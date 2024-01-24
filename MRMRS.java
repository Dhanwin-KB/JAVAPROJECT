import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class MoodFrame {
    private static JFrame mainFrame;
    private static JFrame moodFrame;
    private static JFrame songFrame;
    private static JTable songTable;
    private static DefaultTableModel songTableModel;
    private static String selectedMood;

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
    
    
    private static class SongTableModel extends DefaultTableModel {
        // This list will store changes made in the table
        private List<Object[]> changes = new ArrayList<>();

        // Constructor with column names and data
        public SongTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
            // Add a listener to track changes in the table model
            addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    // If the change is in a valid row and column
                    if (row != TableModelEvent.HEADER_ROW && column != TableModelEvent.ALL_COLUMNS) {
                        Object[] rowData = new Object[getColumnCount()];
                        for (int i = 0; i < getColumnCount(); i++) {
                            rowData[i] = getValueAt(row, i);
                        }
                        changes.add(rowData);
                    }
                }
            });
        }

        // Getter for changes
        public List<Object[]> getChanges() {
            return changes;
        }

        // Clear the changes list
        public void clearChanges() {
            changes.clear();
        }
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
                // Check for empty fields
                if (nameField.getText().isEmpty() || languageComboBox.getSelectedItem() == null || genreComboBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(mainFrame, "Please fill in all fields.");
                } else {
                    // Check for numerics in the name
                    if (containsNumericCharacters(nameField.getText())) {
                        JOptionPane.showMessageDialog(mainFrame, "Invalid name. Please enter a name without numeric characters.");
                    } else {
                        // Connect to the database and insert registration data
                        registerUser(nameField.getText(), (String) languageComboBox.getSelectedItem(), (String) genreComboBox.getSelectedItem());
                    }
                }
            }

			private boolean containsNumericCharacters(String name) {
	            return name.matches(".\\d.");

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
                        // Check if the username contains only non-numeric characters
                        if (containsNumericCharacters(username)) {
                            JOptionPane.showMessageDialog(mainFrame, "Invalid username. Please enter a username without numeric characters.");
                        } else {
                            // Check if the username exists in the database
                            if (isUsernameExists(username)) {
                                // Handle returning user logic (e.g., show mood page)
                                createAndShowMoodPage("Returning User: " + username, "", "");
                            } else {
                                JOptionPane.showMessageDialog(mainFrame, "Username not found. Please try again.");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "Please enter a username.");
                    }
                }
            }

			private boolean containsNumericCharacters(String username) {
		         return username.matches(".\\d.");
			}
        });

        // Method to check if the username contains numeric characters



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
        JButton happyButton = createStyledButtonWithBorder("happy", new Color(52, 152, 219));
        JButton sadButton = createStyledButtonWithBorder("sad", new Color(52, 152, 219));
        JButton calmButton = createStyledButtonWithBorder("calm", new Color(52, 152, 219));
        JButton excitedButton = createStyledButtonWithBorder("excited", new Color(52, 152, 219));
        JButton angryButton = createStyledButtonWithBorder("angry", new Color(52, 152, 219));
        JButton relaxedButton = createStyledButtonWithBorder("relaxed", new Color(52, 152, 219));
        
        happyButton.addActionListener(createMoodButtonListener("happy"));
        sadButton.addActionListener(createMoodButtonListener("sad"));
        calmButton.addActionListener(createMoodButtonListener("calm"));
        excitedButton.addActionListener(createMoodButtonListener("excited"));
        angryButton.addActionListener(createMoodButtonListener("angry"));
        relaxedButton.addActionListener(createMoodButtonListener("relaxed"));

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

        happyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAndShowSongList("happy");
            }
        });

        sadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAndShowSongList("sad");
            }
        });

        calmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAndShowSongList("calm");
            }
        });

        excitedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAndShowSongList("excited");
            }
        });

        angryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAndShowSongList("angry");
            }
        });

        relaxedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAndShowSongList("relaxed");
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
    

    private static ActionListener createMoodButtonListener(String mood) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedMood = mood;
                createAndShowSongList(selectedMood);
            }
        };
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
    	
        JButton playButton = createStyledButton("Play", new Color(46, 204, 113));
        JButton removeButton = createStyledButton("Remove", new Color(231, 76, 60));
        JButton addButton = createStyledButton("Add Song", new Color(52, 152, 219));
        JButton updateButton = createStyledButton("Add Song", new Color(52, 152, 219));
    	
        songFrame = new JFrame("Song List - " + mood);
        songFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Dummy data for the song list
        Object[][] data = fetchSongDataFromDatabase(mood);

       // Column names for the table
       String[] columnNames = {"Song Title", "Artist", "Duration", "Link"};

       // Create a table model
      songTableModel = new DefaultTableModel(data, columnNames);

        // Create a JTable with the model
       songTable = new JTable(songTableModel);
       
       updateButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               // Update the records in the database based on the changes in the table
               updateSongsInDatabase(((MoodFrame.SongTableModel) songTableModel).getChanges());
               // Clear the changes list in the table model
               ((MoodFrame.SongTableModel) songTableModel).clearChanges();
           }
       });
       songTable.setBackground(new Color(44, 62, 80)); // Set dark background
       songTable.getTableHeader().setBackground(new Color(52, 152, 219));
       songTable.getTableHeader().setForeground(Color.WHITE);

       // Customize the row height for better aesthetics
       songTable.setRowHeight(30);

       // Add the table to a scroll pane
       JScrollPane scrollPane = new JScrollPane(songTable);
       songFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create buttons for actions

        
        playButton.addActionListener(createPlayButtonListener());
        removeButton.addActionListener(createRemoveButtonListener());
        updateButton.addActionListener(createUpdateButtonListener());
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show a dialog to add a new song
                showAddSongDialog();
            }
        });
        
        
        songFrame.setSize(600, 400);
        songFrame.setLocationRelativeTo(moodFrame);
        songFrame.setVisible(true);
        

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
        

        // Add action listeners for the buttons
        playButton.addActionListener(createPlayButtonListener());
        removeButton.addActionListener(createRemoveButtonListener());
        addButton.addActionListener(createAddButtonListener());
        
        playButton.addActionListener(createPlayButtonListener());
        removeButton.addActionListener(createRemoveButtonListener());
        addButton.addActionListener(createAddButtonListener());
        updateButton.addActionListener(createUpdateButtonListener());
    }
    
    private static ActionListener createUpdateButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement update action
                updateSongsInDatabase(((MoodFrame.SongTableModel) songTableModel).getChanges());
                // Clear the changes list in the table model
                ((MoodFrame.SongTableModel) songTableModel).clearChanges();
            }
        };
    }
    

    private static void updateSongsInDatabase(List<Object[]> changes) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Mood", "root", "hirangeo@13");

            // Iterate through the changes list and update corresponding records in the database
            for (Object[] change : changes) {
                String query = "UPDATE songs SET title=?, artist=?, duration=?, link=?, mood=? WHERE title=? AND artist=? AND duration=? AND link=?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, (String) change[0]); // new title
                ps.setString(2, (String) change[1]); // new artist
                ps.setString(3, (String) change[2]); // new duration
                ps.setString(4, (String) change[3]); // new link
                ps.setString(5, selectedMood); // existing mood (you might want to change this if mood is editable)
                ps.setString(6, (String) change[0]); // existing title
                ps.setString(7, (String) change[1]); // existing artist
                ps.setString(8, (String) change[2]); // existing duration
                ps.setString(9, (String) change[3]); // existing link

                int i = ps.executeUpdate();
                if (i > 0) {
                    JOptionPane.showMessageDialog(songFrame, "Song has been updated successfully");
                } else {
                    JOptionPane.showMessageDialog(songFrame, "Error updating song. Please try again.");
                }

                ps.close();
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(songFrame, "Error updating song. Please try again.");
        }
    }
    
    private static Object[][] fetchSongDataFromDatabase(String mood) {
        try {
        	
        	
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Mood", "root", "hirangeo@13");
            String query = "SELECT title, artist, duration, link, language FROM songs WHERE mood=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, mood.toLowerCase());
            ResultSet rs = ps.executeQuery();
            
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            
            List<Object[]> dataList = new ArrayList<>();

            while (rs.next()) {
                // Create an array to hold the data for each row
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                dataList.add(row);
            }


            // Create a 2D array to hold the data
            Object[][] data = new Object[dataList.size()][columnCount];
            for (int i = 0; i < dataList.size(); i++) {
                data[i] = dataList.get(i);
            }


            // Close resources
            rs.close();
            ps.close();
            con.close();

            return data;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(songFrame, "Error fetching song data. Please try again.");
            return new Object[0][0];
        }
        
        
        
    }
    
    private static void showAddSongDialog() {
        // Create a dialog to add a new song
        JDialog addSongDialog = new JDialog(songFrame, "Add Song", true);
        addSongDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Create components for the add song dialog
        JTextField titleField = createStyledTextField();
        JTextField artistField = createStyledTextField();
        JTextField durationField = createStyledTextField();
        JTextField linkField = createStyledTextField();
        

        JButton addButton = createStyledButton("Add", new Color(52, 152, 219));
        JButton cancelButton = createStyledButton("Cancel", new Color(231, 76, 60));

        // Add action listener for the Add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add the song to the table and database
                addSongToTableAndDatabase(titleField.getText(), artistField.getText(), durationField.getText(), linkField.getText(),selectedMood);
                addSongDialog.dispose();
            }
        });

        // Add action listener for the Cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSongDialog.dispose();
            }
        });

        // Create a panel for the components
        JPanel addSongPanel = new JPanel(new GridLayout(5, 2));
        addSongPanel.add(createStyledLabel("Title:"));
        addSongPanel.add(titleField);
        addSongPanel.add(createStyledLabel("Artist:"));
        addSongPanel.add(artistField);
        addSongPanel.add(createStyledLabel("Duration:"));
        addSongPanel.add(durationField);
        addSongPanel.add(createStyledLabel("Link:"));
        addSongPanel.add(linkField);
        addSongPanel.add(addButton);
        addSongPanel.add(cancelButton);

        // Set background color
        addSongPanel.setBackground(new Color(44, 62, 80));

        // Add the panel to the dialog
        addSongDialog.getContentPane().add(addSongPanel);
        addSongDialog.setSize(300, 200);
        addSongDialog.setLocationRelativeTo(songFrame);
        addSongDialog.setVisible(true);
    }
    private static void addSongToTableAndDatabase(String title, String artist, String duration, String link,String mood) {
        // Add the song to the table
        songTableModel.addRow(new Object[]{title, artist, duration, link,mood});

        // Add the song to the database
        addSongToDatabase(title, artist, duration, link,mood);

        
    }
    
    private static ActionListener createPlayButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement play action
                int selectedRow = songTable.getSelectedRow();
                if (selectedRow != -1) {
                    String songLink = (String) songTableModel.getValueAt(selectedRow, 3);
                    openWebPage(songLink);
                }
            }
        };
    }
    
    private static ActionListener createRemoveButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement remove action
                int selectedRow = songTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the data of the selected row
                    String title = (String) songTableModel.getValueAt(selectedRow, 0);
                    String artist = (String) songTableModel.getValueAt(selectedRow, 1);
                    String duration = (String) songTableModel.getValueAt(selectedRow, 2);
                    String link = (String) songTableModel.getValueAt(selectedRow, 3);

                    // Remove the row from the table
                    songTableModel.removeRow(selectedRow);

                    // Remove the song from the database
                    removeSongFromDatabase(title, artist, duration, link);
                } else {
                    JOptionPane.showMessageDialog(songFrame, "Select a song to remove.");
                }
            }
        };
    }
    
    private static void removeSongFromDatabase(String title, String artist, String duration, String link) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Mood", "root", "hirangeo@13");
            String query = "DELETE FROM songs WHERE title=? AND artist=? AND duration=? AND link=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, title);
            ps.setString(2, artist);
            ps.setString(3, duration);
            ps.setString(4, link);

            int i = ps.executeUpdate();
            if (i > 0) {
                JOptionPane.showMessageDialog(songFrame, "Song has been removed successfully");
            } else {
                JOptionPane.showMessageDialog(songFrame, "Error removing song. Please try again.");
            }

            ps.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(songFrame, "Error removing song. Please try again.");
        }
    }
    
    private static ActionListener createAddButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement add song action
                // For simplicity, adding a dummy row
//                songTableModel.addRow(new Object[]{"New Song", "New Artist", "3:00", "https://www.youtube.com/watch?v=jkl012"});

                // Call the appropriate method based on the selected mood

            }
        };
    }
    private static void addSongToDatabase(String title, String artist, String duration, String link,String mood) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Mood", "root", "hirangeo@13");
            String query = "INSERT INTO songs (title,artist,duration,link,mood) VALUES (?, ?, ?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, title);
            ps.setString(2, artist);
            ps.setString(3, duration);
            ps.setString(4, link);
            ps.setString(5, mood);
            
            int i = ps.executeUpdate();
//            JOptionPane.showMessageDialog(songFrame, i + " Song has been added successfully");
            ps.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(songFrame, "Error adding song. Please try again.");
        }
    }

    private static void openWebPage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void registerUser(String name, String language, String genre) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Mood", "root", "hirangeo@13");
            String query = "INSERT INTO Registrations (name, language, genre) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, language);
            ps.setString(3, genre);
            int i = ps.executeUpdate();
            JOptionPane.showMessageDialog(mainFrame, i + " Record has been added successfully");
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame, "Error registering user. Please try again.");
        }
    }

    private static boolean isUsernameExists(String username) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Mood", "root", "hirangeo@13");
            String query = "SELECT * FROM Registrations WHERE name = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();
            rs.close();
            ps.close();
            con.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
