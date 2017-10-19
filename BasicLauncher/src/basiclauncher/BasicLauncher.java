/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basiclauncher;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.Pane;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author giacomosantarelli
 */
public class BasicLauncher implements ConnectorDelegate 
{
    Connector connector = null;
    ArrayList<JTextField> textFieldArrayList = new ArrayList<>();

    //ListView<String> list = null;
    Pane root;
    
    public void populateList()
    {
        if(connector != null && connector.nodes != null)
        {
            for(int i=0; i<connector.nodes.size();i++) {
                textFieldArrayList.get(i).setText(connector.nodes.get(i).name);
            }
        }
    }
    
    public boolean namesAreCorrect(List<String> list,String s)
    {
        for(int i=0;i<list.size();i++){
            if(s.equals(list.get(i))){
                return false;
            }
        }
        return true;
    }
    
    public void start(String[] args) throws UnknownHostException, RemoteException
    {   
        Exception e = new Exception("Argomenti non validi");
        
        if(args[0].equals("server"))
        {                
            
            JFrame frame = new JFrame("Registrazione");
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
            
            JLabel label = new JLabel("Giocatori in attesa");
            label.setForeground(Color.blue);
            panel.add(label);
            
            JButton start = new JButton();
            start.setText("Inizia Partita");
            start.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) 
                {
                    try 
                    {
                        ((ServerConnector)connector).start();
                        frame.setVisible(false); //NASCONDE PANNELLO
                    } 
                    catch (RemoteException ex) 
                    {
                        Logger.getLogger(BasicLauncher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            for(int i=0; i<6;i++) {
                JTextField textField = new JTextField();
                textField.setEditable(false);
                textFieldArrayList.add(textField);
                panel.add(textField);
            }
            
            
            panel.add(start);
            frame.add(panel);
            frame.setSize(150,250);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            
            connector = new ServerConnector(args[1]); 
            connector.delegate=this;
            
            populateList();
        }
        
        else if(args[0].equals("client")){
                connector = new ClientConnector(args[1],args[2]);  
                connector.delegate = this;
                ((ClientConnector)connector).connectToServer();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException, UnknownHostException {
        
        BasicLauncher launcher = new BasicLauncher();
        launcher.start(args);
    }

    @Override
    public void done(int index, ArrayList<Node> nodes) 
    {
        // Lanciare il gioco
        try 
        {
            ArrayList<String> parameters = new ArrayList<>();
            parameters.add("java");
            parameters.add("-jar");
            parameters.add("Monopoly.jar");
            parameters.add(Integer.toString(index));
            
            for(int i=0; i<nodes.size(); i++) 
            {
                Node n = nodes.get(i);
                parameters.add(n.name);
                parameters.add(n.ip);
                
                System.out.println(n.name);
            }
            
            System.err.println(Thread.currentThread().getId());
            
            /*
            Process ps = Runtime.getRuntime().exec(parameters.toArray(new String[parameters.size()]));
            ps.waitFor();
            java.io.InputStream is=ps.getInputStream();
            byte b[]=new byte[is.available()];
            is.read(b,0,b.length);
            System.out.println(new String(b));*/
            
            ProcessBuilder pb = new ProcessBuilder(parameters.toArray(new String[parameters.size()]));
            pb.start();
            
        } catch (Throwable ex) {
            Logger.getLogger(BasicLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void nodeWasAdded(Node node) 
    {
        for(Node n:connector.nodes) {
            System.err.println(n.name);
        }
        populateList();
    }
    
}
