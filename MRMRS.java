import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
public class MRMRS 
{
    private static JFrame mainFrame;
    private static JFrame moodFrame;
    private static JFrame songFrame;

    private static DefaultTableModel songTableModel;

    public static void main(String[] args) {
        try {
            // Set Nimbus with a dark theme
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("nimbusBase", new Color(18, 30, 49));
            UIManager.put("nimbusBlueGrey", new Color(28, 41, 61));
            UIManager.put("control", new Color(44, 62, 80));
            UIManager.put("text", new Color(236, 240, 241));
            UIManager.put("nimbusSelectionBackground", new Color(52, 152, 219));
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            createAndShowRegisterPage();
        });
    }

    private static void createAndShowRegisterPage() {
        mainFrame = new JFrame("Register Page");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField nameField = createStyledTextField();

        // Create JComboBox for language
        String[] languages = {"Malayalam", "Tamil", "English", "Hindi", "Kannada"};
        JComboBox<String> languageComboBox = createStyledComboBox(languages);

        // Create JComboBox for genre
        String[] genres = {"Pop", "Hip-hop", "R&B", "Classical", "Instrumental"};
        JComboBox<String> genreComboBox = createStyledComboBox(genres);

        JButton registerButton = createStyledButton("Register", new Color(52, 152, 219));
        JButton returningUserButton = createStyledButton("Login", new Color(52, 152, 219));

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAndShowMoodPage(nameField.getText(), (String) languageComboBox.getSelectedItem(), (String) genreComboBox.getSelectedItem());
            }
        });

        returningUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ask for username for returning users
                JTextField usernameField = createStyledTextField();
                Object[] message = {
                        "Enter your username:", usernameField
                };

                int option = JOptionPane.showConfirmDialog(mainFrame, message, "Returning User", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String username = usernameField.getText();
                    if (username != null && !username.isEmpty()) {
                        // Handle returning user logic (not implemented in this example)
                        createAndShowMoodPage("Returning User: " + username, "", "");
                    }
                }
            }
        });

        panel.add(createStyledLabel("Name:"));
        panel.add(nameField);
        panel.add(createStyledLabel("Language:"));
        panel.add(languageComboBox);
        panel.add(createStyledLabel("Genre:"));
        panel.add(genreComboBox);
        panel.add(registerButton);
        panel.add(returningUserButton);

        mainFrame.getContentPane().add(panel);
        mainFrame.setSize(400, 200);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private static JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBackground(new Color(44, 62, 80));
        comboBox.setForeground(new Color(255, 255, 255));
        ListCellRenderer<? super String> renderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(new Color(44, 62, 80));
                setForeground(new Color(255, 255, 255));
    
                if (isSelected) {
                    setBackground(new Color(52, 152, 219)); // Highlight color
                    setForeground(Color.WHITE);
                }
    
                return this;
            }
        };
    
        comboBox.setRenderer(renderer);
    
        return comboBox;
    }

    private static void createAndShowMoodPage(String name, String language, String genre) {
        moodFrame = new JFrame("Mood Selection");
        moodFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JButton happyButton = createStyledButtonWithBorder("Happy", new Color(52, 152, 219));
        JButton sadButton = createStyledButtonWithBorder("Sad", new Color(52, 152, 219));
        JButton calmButton = createStyledButtonWithBorder("Calm", new Color(52, 152, 219));
        JButton excitedButton = createStyledButtonWithBorder("Excited", new Color(52, 152, 219));
        JButton angryButton = createStyledButtonWithBorder("Angry", new Color(52, 152, 219));
        JButton relaxedButton = createStyledButtonWithBorder("Relaxed", new Color(52, 152, 219));

        happyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAndShowSongList("Happy Mood");
            }
        });

        sadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAndShowSongList("Sad Mood");
            }
        });

        calmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAndShowSongList("Calm Mood");
            }
        });

        excitedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAndShowSongList("Excited Mood");
            }
        });

        angryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAndShowSongList("Angry Mood");
            }
        });

        relaxedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAndShowSongList("Relaxed Mood");
            }
        });

        panel.add(happyButton);
        panel.add(sadButton);
        panel.add(calmButton);
        panel.add(excitedButton);
        panel.add(angryButton);
        panel.add(relaxedButton);

        moodFrame.getContentPane().add(panel);
        moodFrame.setSize(300, 200);
        moodFrame.setLocationRelativeTo(mainFrame);
        moodFrame.setVisible(true);
    }

    private static JButton createStyledButtonWithBorder(String text, Color bgColor) {
        JButton button = createStyledButton(text, bgColor);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Added border
        return button;
    }

    private static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(new Color(255, 255, 255));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }

    private static JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(255, 255, 255));
        return label;
    }

    private static JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(new Color(44, 62, 80));
        textField.setForeground(new Color(255, 255, 255));
        return textField;
    }

    private static void createAndShowSongList(String mood) {
        songFrame = new JFrame("Song List - " + mood);
        songFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Dummy data for the song list
        Object[][] data = {
                {"Song 1", "Artist 1", "3:45", "https://www.youtube.com/watch?v=abc123", "Pop", "English"},
                {"Song 2", "Artist 2", "4:20", "https://www.youtube.com/watch?v=def456", "Pop", "English"},
                {"Song 3", "Artist 3", "3:30", "https://www.youtube.com/watch?v=ghi789", "Pop", "English"}
        };

        // Column names for the table
        String[] columnNames = {"Song Title", "Artist", "Duration", "Link", "Genre", "Language"};

        // Create a table model 
        songTableModel = new DefaultTableModel(data, columnNames);

        // Create a JTable with the model
        JTable songTable = new JTable(songTableModel);
        songTable.setBackground(new Color(44, 62, 80)); // Set dark background
        songTable.getTableHeader().setBackground(new Color(52, 152, 219));
        songTable.getTableHeader().setForeground(Color.WHITE);

        // Customize the row height for better aesthetics
        songTable.setRowHeight(30);

        String[] languages = {"Malayalam", "Tamil", "English", "Hindi", "Kannada"};
        JComboBox<String> languageComboBox = new JComboBox<>(languages);
        TableColumn languageColumn = songTable.getColumnModel().getColumn(5); // Assuming "Language" is the 5th column
        languageColumn.setCellEditor(new DefaultCellEditor(languageComboBox));

        // Create JComboBox for genre and set it as the editor for the "Genre" column
        String[] genres = {"Pop", "Hip-hop", "R&B", "Classical", "Instrumental"};
        JComboBox<String> genreComboBox = new JComboBox<>(genres);
        TableColumn genreColumn = songTable.getColumnModel().getColumn(4); // Assuming "Genre" is the 4th column
        genreColumn.setCellEditor(new DefaultCellEditor(genreComboBox));

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(songTable);
        songFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create buttons for actions
        JButton playButton = createStyledButton("Play", new Color(46, 204, 113));
        JButton removeButton = createStyledButton("Remove", new Color(231, 76, 60));
        JButton addButton = createStyledButton("Add Song", new Color(52, 152, 219));

        // Add action listeners for the buttons
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement play action
                int selectedRow = songTable.getSelectedRow();
                if (selectedRow != -1) {
                    String songLink = (String) songTableModel.getValueAt(selectedRow, 3);
                    openWebPage(songLink);
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement remove action
                int selectedRow = songTable.getSelectedRow();
                if (selectedRow != -1) {
                    songTableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(songFrame, "Select a song to remove.");
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement add song action
                // For simplicity, adding a dummy row
                songTableModel.addRow(new Object[]{"New Song", "New Artist", "3:00", "https://www.youtube.com/watch?v=jkl012"});
            }
        });

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(playButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(addButton);

        // Add the button panel to the frame
        songFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        songFrame.setSize(600, 400);
        songFrame.setLocationRelativeTo(moodFrame);
        songFrame.setVisible(true);
    }

    private static void openWebPage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
