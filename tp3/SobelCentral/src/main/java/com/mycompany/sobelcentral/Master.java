/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sobelcentral;

/**
 *
 * @author Usuario
 */


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.imageio.ImageIO;

public class Master {
    private static final int NUM_WORKERS = 4;
    private static final int[] WORKER_PORTS = {5000, 5001, 5002, 5003};
    private static final String INPUT_IMAGE = "imagen.jpg";
    private static final String OUTPUT_IMAGE = "output_imageDistributedWithReassign.jpg";
    private static final int RETRY_LIMIT = 3;
    private static final int TIMEOUT_MS = 5000;

    public static void main(String[] args) throws Exception {
        BufferedImage image = ImageIO.read(new File(INPUT_IMAGE));
        int width = image.getWidth();
        int height = image.getHeight();

        int rowsPer = height / NUM_WORKERS;
        int extra = height % NUM_WORKERS;

        ExecutorService exec = Executors.newFixedThreadPool(NUM_WORKERS);
        List<Future<BufferedImage>> futures = new ArrayList<>();

        // Enviar a cada worker su franja
        for (int i = 0; i < NUM_WORKERS; i++) {
            int start = i * rowsPer;
            int count = rowsPer + (i == NUM_WORKERS - 1 ? extra : 0);
            BufferedImage sub = image.getSubimage(0, start, width, count);
            futures.add(exec.submit(new WorkerTask(sub, start, count, WORKER_PORTS[i])));
        }

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Recolección con reintentos y reasignación entre diferentes puertos
        for (int i = 0; i < NUM_WORKERS; i++) {
            int start = i * rowsPer;
            int count = rowsPer + (i == NUM_WORKERS - 1 ? extra : 0);
            boolean done = false;
            int attempts = 0;
            // Primer intento con Future
            while (!done && attempts < RETRY_LIMIT) {
                try {
                    BufferedImage proc = futures.get(i).get(TIMEOUT_MS, TimeUnit.MILLISECONDS);
                    result.setRGB(0, start, width, count, proc.getRGB(0, 0, width, count, null, 0, width), 0, width);
                    done = true;
                } catch (TimeoutException | InterruptedException | ExecutionException e) {
                    attempts++;
                    System.out.println("Worker " + i + " fallo (intento " + attempts + ")");
                    // Reasignar a otros puertos
                    for (int port : WORKER_PORTS) {
                        if (port == WORKER_PORTS[i]) continue;
                        try {
                            BufferedImage sub = image.getSubimage(0, start, width, count);
                            BufferedImage proc = new WorkerTask(sub, start, count, port).call();
                            result.setRGB(0, start, width, count, proc.getRGB(0, 0, width, count, null, 0, width), 0, width);
                            System.out.println("Reasignado franja [" + start + "," + count + "] a puerto " + port);
                            done = true;
                            break;
                        } catch (Exception ex) {
                            System.out.println("Worker en puerto " + port + " fallo: " + ex.getMessage());
                        }
                    }
                }
            }
            if (!done) {
                System.out.println("No se procesó franja [" + start + "," + count + "] después de " + RETRY_LIMIT + " intentos.");
            }
        }

        ImageIO.write(result, "jpg", new File(OUTPUT_IMAGE));
        exec.shutdown();
    }

    static class WorkerTask implements Callable<BufferedImage> {
        private final BufferedImage subImg;
        private final int start, count, port;
        public WorkerTask(BufferedImage subImg, int start, int count, int port) {
            this.subImg = subImg;
            this.start = start;
            this.count = count;
            this.port = port;
        }
        @Override
        public BufferedImage call() throws Exception {
            try (Socket s = new Socket("localhost", port);
                 ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(s.getInputStream())) {
                ImageIO.write(subImg, "png", out);
                out.flush();
                return ImageIO.read(in);
            }
        }
    }
}

