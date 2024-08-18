import javax.swing.*;
import java.io.*;

public class TextAnalyzer extends JFrame {
    private JTextArea textArea;
    private final JFileChooser fileChooser;
    private File currentFile;

    public TextAnalyzer() {
        // Configuración del JFrame
        setTitle("Análisis de Texto");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        fileChooser = new JFileChooser();

        // Crear el menú
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        JMenu menuEditar = new JMenu("Editar");

        // Opciones del menú Archivo
        JMenuItem abrir = new JMenuItem("Abrir");
        JMenuItem guardar = new JMenuItem("Guardar");
        JMenuItem guardarComo = new JMenuItem("Guardar Como");

        // Agregar eventos a las opciones de menú
        abrir.addActionListener(e -> abrirArchivo());
        guardar.addActionListener(e -> guardarArchivo(false));
        guardarComo.addActionListener(e -> guardarArchivo(true));

        menuArchivo.add(abrir);
        menuArchivo.add(guardar);
        menuArchivo.add(guardarComo);

        // Opciones del menú Editar
        JMenuItem copiar = new JMenuItem("Copiar");
        JMenuItem cortar = new JMenuItem("Cortar");
        JMenuItem pegar = new JMenuItem("Pegar");
        JMenuItem buscar = new JMenuItem("Buscar");
        JMenuItem reemplazar = new JMenuItem("Reemplazar");

        copiar.addActionListener(e -> textArea.copy());
        cortar.addActionListener(e -> textArea.cut());
        pegar.addActionListener(e -> textArea.paste());
        buscar.addActionListener(e -> buscarTexto());
        reemplazar.addActionListener(e -> reemplazarTexto());

        menuEditar.add(copiar);
        menuEditar.add(cortar);
        menuEditar.add(pegar);
        menuEditar.add(buscar);
        menuEditar.add(reemplazar);

        menuBar.add(menuArchivo);
        menuBar.add(menuEditar);
        setJMenuBar(menuBar);

        add(new JScrollPane(textArea), "Center");

        // Botón Procesar
        JButton procesarButton = new JButton("Procesar");
        procesarButton.addActionListener(e -> procesarTexto());
        add(procesarButton, "South");
    }

    private void abrirArchivo() {
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                textArea.read(reader, null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void guardarArchivo(boolean guardarComo) {
        if (guardarComo || currentFile == null) {
            int option = fileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
            textArea.write(writer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void buscarTexto() {
        String palabra = JOptionPane.showInputDialog(this, "Ingrese palabra a buscar:");
        if (palabra != null && !palabra.isEmpty()) {
            String contenido = textArea.getText();
            int index = contenido.indexOf(palabra);
            if (index != -1) {
                textArea.select(index, index + palabra.length());
            } else {
                JOptionPane.showMessageDialog(this, "Palabra no encontrada.");
            }
        }
    }

    private void reemplazarTexto() {
        String palabra = JOptionPane.showInputDialog(this, "Ingrese palabra a reemplazar:");
        if (palabra != null && !palabra.isEmpty()) {
            String reemplazo = JOptionPane.showInputDialog(this, "Reemplazar con:");
            textArea.setText(textArea.getText().replace(palabra, reemplazo));
        }
    }

    private void procesarTexto() {
        String texto = textArea.getText().trim();

        if (!texto.isEmpty()) {
            // Análisis del texto
            int textLength = texto.length();
            String[] words = texto.split("\\s+");
            int wordCount = words.length;
            char firstLetter = texto.charAt(0);
            char lastLetter = texto.charAt(textLength - 1);
            char middleLetter = texto.charAt(textLength / 2);
            String firstWord = words[0];
            String lastWord = words[wordCount - 1];
            String middleWord = words[wordCount / 2];

            // Cálculo de repeticiones de vocales (incluyendo tildes)
            int aCount = countOccurrences(texto, 'a') + countOccurrences(texto, 'á') + countOccurrences(texto, 'A') + countOccurrences(texto, 'Á');
            int eCount = countOccurrences(texto, 'e') + countOccurrences(texto, 'é') + countOccurrences(texto, 'E') + countOccurrences(texto, 'É');
            int iCount = countOccurrences(texto, 'i') + countOccurrences(texto, 'í') + countOccurrences(texto, 'I') + countOccurrences(texto, 'Í');
            int oCount = countOccurrences(texto, 'o') + countOccurrences(texto, 'ó') + countOccurrences(texto, 'O') + countOccurrences(texto, 'Ó');
            int uCount = countOccurrences(texto, 'u') + countOccurrences(texto, 'ú') + countOccurrences(texto, 'ü') + countOccurrences(texto, 'U') + countOccurrences(texto, 'Ú') + countOccurrences(texto, 'Ü');

            // Contar palabras con número de caracteres par/impar
            int evenWords = 0;
            int oddWords = 0;
            for (String word : words) {
                if (word.length() % 2 == 0) {
                    evenWords++;
                } else {
                    oddWords++;
                }
            }

            // Traducción a clave murciélago
            StringBuilder claveMurcielago = new StringBuilder();
            for (char c : texto.toLowerCase().toCharArray()) {
                switch (c) {
                    case 'm' -> claveMurcielago.append('6');
                    case 'u' -> claveMurcielago.append('7');
                    case 'r' -> claveMurcielago.append('8');
                    case 'c' -> claveMurcielago.append('3');
                    case 'i' -> claveMurcielago.append('1');
                    case 'e' -> claveMurcielago.append('5');
                    case 'l' -> claveMurcielago.append('4');
                    case 'a' -> claveMurcielago.append('0');
                    case 'g' -> claveMurcielago.append('9');
                    case 'o' -> claveMurcielago.append('2');
                    default -> claveMurcielago.append(c);
                }
            }

            // Mostrar resultados en JOptionPane
            String result = String.format("Longitud del texto: %d\nTotal de palabras: %d\n" +
                            "Primer letra del texto: %c\nÚltima letra del texto: %c\n" +
                            "Letra central del texto: %c\nPrimera palabra: %s\nÚltima palabra: %s\n" +
                            "Palabra central: %s\n\nRepeticiones de 'A/a/á/Á': %d\nRepeticiones de 'E/e/é/É': %d\n" +
                            "Repeticiones de 'I/i/í/Í': %d\nRepeticiones de 'O/o/ó/Ó': %d\nRepeticiones de 'U/u/ú/ü/Ú/Ü': %d\n\n" +
                            "Palabras con caracteres pares: %d\nPalabras con caracteres impares: %d\n\n" +
                            "Traducción a clave murciélago:\n%s",
                    textLength, wordCount, firstLetter, lastLetter, middleLetter, firstWord, lastWord,
                    middleWord, aCount, eCount, iCount, oCount, uCount, evenWords, oddWords, claveMurcielago.toString());

            JOptionPane.showMessageDialog(this, result, "Resultados del Análisis", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese o cargue un texto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int countOccurrences(String text, char character) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c == character) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TextAnalyzer analyzer = new TextAnalyzer();
            analyzer.setVisible(true);
        });
    }
}

