import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageManipulationGUI {

    private BufferedImage originalImage;
    private JLabel imageLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImageManipulationGUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        ImageManipulationGUI imageManipulation = new ImageManipulationGUI();
        imageManipulation.initGUI();
    }

    private void initGUI() {
        JFrame frame = new JFrame("Image Manipulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Create components
        createComponents(frame.getContentPane());

        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }

    private void createComponents(Container container) {
        container.setLayout(new BorderLayout());

        // Create image label
        imageLabel = new JLabel();
        container.add(imageLabel, BorderLayout.CENTER);

        // Create control panel
        JPanel controlPanel = new JPanel(new GridLayout(1, 4));

        // Load image button
        JButton loadImageButton = new JButton("Load Image");
        loadImageButton.addActionListener(e -> loadImage());

        // Black and white filter button
        JButton blackAndWhiteButton = new JButton("Black and White");
        blackAndWhiteButton.addActionListener(e -> applyFilter(this::getBlackAndWhiteColor));

        // Sepia filter button
        JButton sepiaButton = new JButton("Sepia");
        sepiaButton.addActionListener(e -> applyFilter(this::getSepiaColor));

        // Invert filter button
        JButton invertButton = new JButton("Invert");
        invertButton.addActionListener(e -> applyFilter(this::invertColor));

        // Add buttons to control panel
        controlPanel.add(loadImageButton);
        controlPanel.add(blackAndWhiteButton);
        controlPanel.add(sepiaButton);
        controlPanel.add(invertButton);

        container.add(controlPanel, BorderLayout.SOUTH);
    }

    private void loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                originalImage = ImageIO.read(selectedFile);
                updateImageLabel(originalImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void applyFilter(ColorFilter filter) {
        if (originalImage != null) {
            BufferedImage filteredImage = applyFilter(originalImage, filter);
            updateImageLabel(filteredImage);
        }
    }

    private int getBlackAndWhiteColor(int color) {
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;
        int average = (red + green + blue) / 3;
        return (average << 16) | (average << 8) | average;
    }

    private int getSepiaColor(int color) {
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;
        int newRed = (int) Math.min(255, (0.393 * red) + (0.769 * green) + (0.189 * blue));
        int newGreen = (int) Math.min(255, (0.349 * red) + (0.686 * green) + (0.168 * blue));
        int newBlue = (int) Math.min(255, (0.272 * red) + (0.534 * green) + (0.131 * blue));
        return (newRed << 16) | (newGreen << 8) | newBlue;
    }

    private int invertColor(int color) {
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;
        int invertedRed = 255 - red;
        int invertedGreen = 255 - green;
        int invertedBlue = 255 - blue;
        return (invertedRed << 16) | (invertedGreen << 8) | invertedBlue;
    }

    private BufferedImage applyFilter(BufferedImage inputImage, ColorFilter filter) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = inputImage.getRGB(x, y);
                int filteredColor = filter.applyFilter(rgb);
                outputImage.setRGB(x, y, filteredColor);
            }
        }
        return outputImage;
    }

    private void updateImageLabel(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        imageLabel.setIcon(icon);
    }

    // Functional interface for color filters
    private interface ColorFilter {
        int applyFilter(int color);
    }
}
