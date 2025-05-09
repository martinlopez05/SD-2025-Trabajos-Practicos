/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sobelcentral;

/**
 *
 * @author Usuario
 */
// Worker.java
// Worker.java
import static com.sun.tools.javac.tree.TreeInfo.args;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;

public class Worker {

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 5000;
        System.out.println("Worker escuchando en el puerto " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept(); ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream()); ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                    BufferedImage subImage = ImageIO.read(ImageIO.createImageInputStream(in));
                    BufferedImage result = aplicarSobel(subImage);
                    ImageIO.write(result, "png", out); // devuelve la imagen procesada
                    out.flush();
                } catch (Exception e) {
                    System.err.println("Error al procesar la imagen: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Error iniciando el Worker: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static BufferedImage aplicarSobel(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int[][] sobelX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] sobelY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int pixelX = 0;
                int pixelY = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int rgb = img.getRGB(x + j, y + i);
                        int gray = (rgb >> 16) & 0xff;
                        pixelX += gray * sobelX[i + 1][j + 1];
                        pixelY += gray * sobelY[i + 1][j + 1];
                    }
                }
                int mag = Math.min(255, (int) Math.sqrt(pixelX * pixelX + pixelY * pixelY));
                int color = (mag << 16) | (mag << 8) | mag;
                result.setRGB(x, y, color);
            }
        }
        return result;
    }
}
