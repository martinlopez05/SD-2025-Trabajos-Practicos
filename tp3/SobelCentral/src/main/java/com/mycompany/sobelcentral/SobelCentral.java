package com.mycompany.sobelcentral;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SobelCentral {

    public static BufferedImage applySobelFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Máscaras de Sobel (horizontal y vertical)
        int[][] sobelX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] sobelY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int pixelX = 0;
                int pixelY = 0;

                // Aplicar la máscara Sobel a la vecindad de cada píxel
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int pixelColor = image.getRGB(x + j, y + i);
                        int gray = (pixelColor >> 16) & 0xff; // Obtener el valor de gris

                        pixelX += gray * sobelX[i + 1][j + 1];
                        pixelY += gray * sobelY[i + 1][j + 1];
                    }
                }

                // Calcular el valor del píxel final
                int magnitude = (int) Math.min(255, Math.sqrt(pixelX * pixelX + pixelY * pixelY));
                int newPixelColor = (magnitude << 16) | (magnitude << 8) | magnitude;
                result.setRGB(x, y, newPixelColor);
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        File inputFile = new File("imagen.jpg");
        BufferedImage image = ImageIO.read(inputFile);

        BufferedImage sobelImage = applySobelFilter(image);

        File outputFile = new File("output_image.jpg");
        ImageIO.write(sobelImage, "jpg", outputFile);
    }
}
