package com.Ronit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Scanner;

public class Main {

    static final String USER = "root";
    static final String PASS = "Gandhi_op16";

    //ONLY STATICS HERE//

    static Scanner sc = new Scanner(System.in);

    //main table
    static DefaultTableModel tableModel = new DefaultTableModel();
    static JTable table = new JTable(tableModel);

    //main frame
    static JFrame f = new JFrame("Inventory Management System");

    //Transaction List
    static JList<String> list = new JList<>();
    static DefaultListModel<String> listModel = new DefaultListModel<>();

    //Price List
    static JList<Integer> list_price = new JList<>();
    static DefaultListModel<Integer> listModel_price = new DefaultListModel<>();

    //Update list Price
    static JList<Integer>  list_price_updated = new JList<>();
    static DefaultListModel<Integer> listModel_price_updated = new DefaultListModel<>();

    //Delete price list
    static JList<Integer>  list_price_deleted = new JList<>();
    static DefaultListModel <Integer> listModel_price_deleted = new DefaultListModel<>();


    //Row sorter
    static TableRowSorter<DefaultTableModel>sorter = new TableRowSorter<>(tableModel);
    static JTextField search = new HintTextField("Search here");

    //Creating an object of class
    static item item_obj;
    //static Supplier supplier_obj;

    static int m; //total price
    static int c; //sql get total price 
    static int u; //updated price
    static int d; //deleted price
    static int get_row; //get row when mouse click

    //---------------------------//MAIN//-----------------------------------//
    public static void main(String[] args) throws SQLException {

        //DATABASE CONNECTION
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javamini?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",USER,PASS);
        //SQL statements to GET from db
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM mainframe");
        PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM transactionhist");

        //Result object
        ResultSet rs = preparedStatement.executeQuery();
        ResultSet rs1 = preparedStatement1.executeQuery();
        DefaultTableModel tableModel1 = (DefaultTableModel) table.getModel();
        tableModel1.setRowCount(0);

        //Adding columns
        tableModel.addColumn("Id");
        tableModel.addColumn("Item");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Price");
        tableModel.addColumn("Date");
        tableModel.addColumn("Supplier");

        f.setSize(550, 350);
        f.add(new JScrollPane(table));


        table.setGridColor(Color.lightGray);
        table.setRowSorter(sorter); //sorter

        //Initializing Text Fields
        JTextField id = new JTextField();

        JTextField name = new HintTextField("Enter name");
        JTextField quantity = new HintTextField("Enter quantity");
        JTextField price = new HintTextField("Enter price");
        JTextField total_price = new JTextField();

        //dropdown function for supplier --Test
        String[] s = { "Amazon Fresh", "FreshFoods", "Amazon Wholesale", "Flipkart", "Grofers","BigBasket" };
        JComboBox<String> supplier = new JComboBox<>(s);
        supplier.setSelectedIndex(0);

        //Initializing Buttons
        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");
        JButton btnUpdate = new JButton("Update");
        JButton btnReceipt = new JButton("Receipt");

        //UI STARTS-------->
        //setting bounds to text fields
        id.setBounds(50, 270, 100, 25);
        name.setBounds(50, 270, 100, 25);
        quantity.setBounds(50, 300, 100, 25);
        price.setBounds(50, 330, 100, 25);
        supplier.setBounds(50, 360, 100, 25);
        search.setBounds(50, 10, 150, 25);

        //setting bounds and visibility to the final price
        total_price.setBounds(400, 510, 300, 25);
        total_price.setVisible(false);

        //setting bounds to the buttons
        btnAdd.setBounds(200, 270, 100, 25);
        btnUpdate.setBounds(200, 315, 100, 25);
        btnDelete.setBounds(200, 360, 100, 25);
        btnReceipt.setBounds(200, 405, 100, 25);

        //Adding scroll panes to the lists and tables
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(50, 50, 880, 200);
        JScrollPane scrollPane_transaction  = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane_transaction.setBounds(400, 270, 600, 100);

        //Adding all components to main frame
        //panes and lists
        f.setLayout(null);
        f.add(pane);
        f.add(scrollPane_transaction);

        //textFields
        f.add(name);
        f.add(quantity);
        f.add(price);
        f.add(total_price);
        f.add(search);
        f.add(supplier);

        //Buttons
        f.add(btnAdd);
        f.add(btnDelete);
        f.add(btnUpdate);
        f.add(btnReceipt);

        //UI ENDS------>


        //Data entry to main frame from database
        while (rs.next()) {
            int i = rs.getInt("id");
            String d = rs.getString("name_");
            int e = rs.getInt("quantity");
            int f = rs.getInt("price");
            String h = rs.getString("date_");
            String j = rs.getString("supplier");
            c = c + f;
            tableModel1.addRow(new Object[]{i, d, e, f, h, j});
        }
        //initially setting the total price
        total_price.setText("Total amount: " + c);
        //Data entry to transaction hist from database
        while (rs1.next()){
            String history = rs1.getString("history_");
            listModel.addElement(history);
            list.setModel(listModel);
        }

        //BUTTON AND TEXT FUNCTIONS AND ACTIONS

        //search function
        search.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                String query = search.getText();
                filter(query);
            }
        });

        //mouse on click listener, gives parameters of clicked row
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                get_row = table.getSelectedRow();
                //id.setText(tableModel.getValueAt(get_row, 0).toString());
                name.setText(tableModel.getValueAt(get_row, 1).toString());
                quantity.setText(tableModel.getValueAt(get_row, 2).toString());
                price.setText(tableModel.getValueAt(get_row, 3).toString());
                supplier.setSelectedItem(tableModel.getValueAt(get_row,5).toString());
            }
            });


        //Receipt Button Action
        btnReceipt.addActionListener(e -> {
            total_price.setVisible(true);
            m = getTotalPrice();
            u = getUpdatedPrice();
            d = getDeletedPrice();
/*
            for(int i = 0;i<table.getModel().getRowCount();i++)
            {
                list_total_price.add(table.getModel().getValueAt(i,3));

            }
            int sum =0;
            for (int m =0;m<table.getModel().getRowCount();m++){
                System.out.println(Integer.parseInt(String.valueOf(list_total_price.get(m))));
            }
*/
            total_price.setText("Total amount: " + ((m+c+u)-d));
        });

        //Delete Button Action
        btnDelete.addActionListener(e -> {
            deleteItem();

            String id_item = id.getText();
            String name_item = name.getText();
            String quantity_item = quantity.getText();
            String price_item = price.getText();
            String supplier_item = supplier.getItemAt(supplier.getSelectedIndex());
            int id_item_int = Integer.parseInt(id_item);
            int quantity_item_int = Integer.parseInt(quantity_item);
            int price_item_deleted = Integer.parseInt(price_item);

            item_obj = new item(name_item, quantity_item_int, price_item_deleted,supplier_item);
            item_obj.setDate();
            //Transaction History List
            listModel.addElement("Removed Item: "+name_item+","+quantity_item_int+","+price_item_deleted+"On date "+item_obj.getDate()+ " with ID"+id_item+". Supplier: "+supplier_item);
            list.setModel(listModel);

            //Delete from database mainframe table
            String query_delete = "DELETE FROM mainframe WHERE id= ?;";
            try {
                PreparedStatement preparedStmt = connection.prepareStatement(query_delete);
                preparedStmt.setInt(1,(id_item_int));
                preparedStmt.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            //Inserting transaction into transaction history table in DB
            String tran_query = "INSERT INTO transactionhist(history_) VALUES (?)";
            try {
                PreparedStatement preparedStatement113 = connection.prepareStatement(tran_query);
                preparedStatement113.setString(1, "Removed Item: "+name_item+","+quantity_item_int+","+price_item_deleted+"On date "+item_obj.getDate()+ " with ID"+id_item+". Supplier: "+supplier_item);
                preparedStatement113.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            //Deleted price appended to list
            listModel_price_deleted.addElement(price_item_deleted);
            list_price_deleted.setModel(listModel_price_deleted);

        });

        //Update Button Action
        btnUpdate.addActionListener(e -> {

            int i = table.getSelectedRow();
            String amount = tableModel.getValueAt(i, 3).toString();
            int amount_int = Integer.parseInt(amount);

            if(i >= 0)
            {

                //setting new values to the table
                tableModel.setValueAt(name.getText(), i, 1);
                tableModel.setValueAt(quantity.getText(), i, 2);
                tableModel.setValueAt(price.getText(), i, 3);
                tableModel.setValueAt(supplier.getItemAt(supplier.getSelectedIndex()),i,5);

                //getting the updated values
                String id_item = id.getText();
                String name_item = name.getText();
                String quantity_item = quantity.getText();
                String price_item = price.getText();
                String supplier_item = supplier.getItemAt(supplier.getSelectedIndex());
                int quantity_item_int = Integer.parseInt(quantity_item);
                int price_item_updated = Integer.parseInt(price_item);

                item_obj = new item(name_item, quantity_item_int, price_item_updated,supplier_item);
                item_obj.setDate();

                //Transaction History List (appending the updated values)
                listModel.addElement("Updated Item: "+name_item+","+quantity_item_int+","+price_item_updated+" with ID "+id_item+" On date " +item_obj.getDate()+".Supplier "+supplier_item);
                list.setModel(listModel);

                //Add transaction to transaction history table in db
                String tran_query = "INSERT INTO transactionhist(history_) VALUES (?)";
                try {
                    PreparedStatement preparedStatement112 = connection.prepareStatement(tran_query);
                    preparedStatement112.setString(1, "Updated Item: "+name_item+","+quantity_item_int+","+price_item_updated+" with ID "+id_item+" On date " +item_obj.getDate()+".Supplier "+supplier_item);
                    preparedStatement112.execute();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                //Updated price list
                if(price_item_updated>amount_int){
                    listModel_price_updated.addElement(price_item_updated-amount_int);
                }
                else{
                    listModel_price_updated.addElement(-(amount_int-price_item_updated));
                }
                list_price_updated.setModel(listModel_price_updated);

                //Updating mainframe table in db
                String query_update = "UPDATE mainframe SET name_ = ?, quantity = ?, price = ?, supplier = ? WHERE id= ?;";
                try {
                    PreparedStatement preparedStmt = connection.prepareStatement(query_update);
                    preparedStmt.setString(1,name_item);
                    preparedStmt.setInt(2,quantity_item_int);
                    preparedStmt.setInt(3,price_item_updated);
                    preparedStmt.setString(4,supplier_item);
                    preparedStmt.setInt(5,(get_row+1));
                    preparedStmt.execute();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }


            }
            else{
                System.out.println("Update Error");
            }
            //System.out.println(amount);
        });

        //Add Button Action
        btnAdd.addActionListener(e -> {
            String name_item = name.getText();
            String quantity_item = quantity.getText();
            String price_item = price.getText();
            String supplier_item = supplier.getItemAt(supplier.getSelectedIndex());
            int quantity_item_int = Integer.parseInt(quantity_item);
            int price_item_int = Integer.parseInt(price_item);

            //setting parameters to object variables
            item_obj = new item(name_item, quantity_item_int, price_item_int,supplier_item);

            if(tableModel.getRowCount()>0){
                int row = tableModel.getRowCount();
                int m = Integer.parseInt(String.valueOf(tableModel.getValueAt(row-1, 0)));
                item_obj.setId(m+1);
            }else {
                item_obj.setId(1);
            }
            //setting object parameters
            item_obj.setName(name_item);
            item_obj.setQuantity(quantity_item_int);
            item_obj.setPrice(price_item_int);
            item_obj.setDate();

            //Appending row to the table
            tableModel.addRow(new Object[]{item_obj.getId(), item_obj.getName(), item_obj.getQuantity(), item_obj.getPrice(), item_obj.getDate(),supplier_item});

            //Appending row to Db mainframe table
            String query1 = "INSERT INTO mainframe (id, name_, quantity, price, date_, supplier ) VALUES ( ?,?,?,?,?,?)";
            try {
                PreparedStatement preparedStmt = connection.prepareStatement(query1);
                preparedStmt.setInt(1, item_obj.getId());
                preparedStmt.setString(2 ,item_obj.getName());
                preparedStmt.setInt(3, item_obj.getQuantity());
                preparedStmt.setInt(4, item_obj.getPrice());
                preparedStmt.setString(5, item_obj.getDate());
                preparedStmt.setString(6,item_obj.getSupplier());
                preparedStmt.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            //Inserting transaction into transaction history list
            listModel.addElement("Added Item: "+name_item+","+quantity_item_int+","+price_item_int+" with ID "+item_obj.getId()+ " On date "+item_obj.getDate()+".Supplier "+supplier_item);
            list.setModel(listModel);

            //Inserting transaction into transaction history table in DB
            String tran_query = "INSERT INTO transactionhist(history_) VALUES (?)";
            try {
                PreparedStatement preparedStatement11 = connection.prepareStatement(tran_query);
                preparedStatement11.setString(1, "Added Item: "+name_item+","+quantity_item_int+","+price_item_int+" with ID "+item_obj.getId()+" On date " +item_obj.getDate()+".Supplier "+supplier_item);
                preparedStatement11.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            //Adding price to total price list
            listModel_price.addElement(price_item_int);
            list_price.setModel(listModel_price);

            //Setting the textFields to blank
            name.setText("");
            quantity.setText("");
            price.setText("");

        });

        System.out.println("This is your inventory management system :)");
        while(true){
            System.out.println("Select an option to continue:");
            System.out.println("1: Print transaction history\n2: Display inventory GUI \n3: Exit Application");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> getTotalPrice();
                case 2 -> printList();
                case 3 -> {
                    System.out.println("Exiting");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice! Please make a valid choice. \n\n");
            }
        }
    }

    //print list method , which starts the GUI ------ Important method
    public static void printList(){
        f.setVisible(true);
    }
    //delete item method
    public static void deleteItem(){
        int row_id = table.getSelectedRow();
        if(row_id >= 0){
            tableModel.removeRow(row_id);
        }
        else{
            System.out.println("Delete Error");
        }
    }
    //gets total price of added items
    public static int getTotalPrice(){
        int sum = 0;
        for(int x = 0; x < listModel_price.getSize(); x++) {
            int number = listModel_price.getElementAt(x);
            sum+=number;
        }


        return sum;
    }
    //updated price method
    public static int getUpdatedPrice(){
        int sum = 0;
        for(int x = 0; x < listModel_price_updated.getSize(); x++) {
            int number = listModel_price_updated.getElementAt(x);
            sum+=number;
        }

        return sum;
    }
    //deleted price method
    public static int getDeletedPrice(){
        int sum = 0;
        for(int x = 0; x < listModel_price_deleted.getSize(); x++) {
            int number = listModel_price_deleted.getElementAt(x);
            sum+=number;
        }
        return sum;
    }
    //filter method
    public static void filter(String query){
        table.setRowSorter(sorter);

        sorter.setRowFilter(RowFilter.regexFilter(query));

    }

}
