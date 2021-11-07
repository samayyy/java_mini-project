package com.Ronit;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class Main {

    //ONLY STATICS HERE//

    static Scanner sc = new Scanner(System.in);

    //main table
    static DefaultTableModel tableModel = new DefaultTableModel();
    static JTable table = new JTable(tableModel);

    //main frame
    static JFrame f = new JFrame("Inventory Management System");

    //labels
    static JLabel transaction_label = new JLabel();
    static JLabel receipt_label= new JLabel();
    static JLabel main_label= new JLabel();

    //Transaction List
    static JList list = new JList();
    static DefaultListModel listModel = new DefaultListModel();

    //Price List
    static JList list_price = new JList();
    static DefaultListModel listModel_price = new DefaultListModel();

    //Receipt List
    static JList list_receipt = new JList();
    static DefaultListModel listModel_receipt = new DefaultListModel();

    //Update list Price
    static JList list_price_updated = new JList();
    static DefaultListModel listModel_price_updated = new DefaultListModel();

    //Delete price list
    static JList list_price_deleted = new JList();
    static DefaultListModel listModel_price_deleted = new DefaultListModel();

    //Row sorter
    static TableRowSorter sorter = new TableRowSorter<>(tableModel);


    //Creating an object of class
    static item item_obj;

    static int m; //total price
    static int u; //updated price
    static int d; //deleted price
    static  int x; //index of list_receipt
    static int get_row; //get row when mouse click

    //---------------------------//MAIN//-----------------------------------//
    public static void main(String[] args) {

        //Adding columns
        tableModel.addColumn("Id");
        tableModel.addColumn("Item");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Price");
        tableModel.addColumn("Date");

        f.setSize(550, 350);
        f.add(new JScrollPane(table));

        table.setGridColor(Color.lightGray);
        table.setRowSorter(sorter);

        //setting labels
        transaction_label.setText("Transaction history");
        transaction_label.setOpaque(true);
        transaction_label.setVisible(true);




        //Initializing Text Fields
        JTextField id = new JTextField();
        JTextField search = new HintTextField("Search here");
        JTextField name = new HintTextField("Enter name");
        JTextField quantity = new HintTextField("Enter quantity");
        JTextField price = new HintTextField("Enter price");
        JTextField total_price = new JTextField();

        //Initializing Buttons
        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");
        JButton btnUpdate = new JButton("Update");
        JButton btnReceipt = new JButton("Receipt");

        //setting bounds to text fields
        id.setBounds(50, 270, 100, 25);
        name.setBounds(50, 270, 100, 25);
        quantity.setBounds(50, 300, 100, 25);
        price.setBounds(50, 330, 100, 25);
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
        scrollPane_transaction.setBounds(400, 270, 500, 100);
        transaction_label.setLabelFor(scrollPane_transaction);

        JScrollPane scrollPane_receipt  = new JScrollPane(list_receipt, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane_receipt.setBounds(400, 400, 300, 100);
        scrollPane_receipt.setVisible(false);


        //Adding all components to main frame
        //panes and lists
        f.setLayout(null);
        f.add(pane);
        f.add(scrollPane_transaction);
        f.add(scrollPane_receipt);
        f.add(transaction_label);

        //textFields
        f.add(name);
        f.add(quantity);
        f.add(price);
        f.add(total_price);
        f.add(search);

        //Buttons
        f.add(btnAdd);
        f.add(btnDelete);
        f.add(btnUpdate);
        f.add(btnReceipt);


        listModel_receipt.addElement("ITEM-------QUANTITY-------PRICE-------DATE");
        list_receipt.setModel(listModel_receipt);

        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(search.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(search.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(search.getText());
            }
            public void search(String str) {
                if (str.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter(str));
                }
            }
        });

        //mouse on click listener, gives parameters of clicked row
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                get_row = table.getSelectedRow();
                id.setText(tableModel.getValueAt(get_row, 0).toString());
                name.setText(tableModel.getValueAt(get_row, 1).toString());
                quantity.setText(tableModel.getValueAt(get_row, 2).toString());
                price.setText(tableModel.getValueAt(get_row, 3).toString());
            }
            });

        list_receipt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                x = list_receipt.getSelectedIndex();
            }
        });

        //Receipt Button Action
        btnReceipt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                scrollPane_receipt.setVisible(true);
                total_price.setVisible(true);
                m = getTotalPrice();
                u = getUpdatedPrice();
                d = getDeletedPrice();
                total_price.setText("Total amount: " + ((m+u)-d));

            }
        });

        //Delete Button Action
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteItem();

                String id_item = id.getText();
                String name_item = name.getText();
                String quantity_item = quantity.getText();
                String price_item = price.getText();
                int quantity_item_int = Integer.parseInt(quantity_item);
                int price_item_deleted = Integer.parseInt(price_item);

                //Transaction History List
                listModel.addElement("Removed Item: "+name_item+","+quantity_item_int+","+price_item_deleted+"On date "+item_obj.getDate()+ " with ID"+id_item);
                list.setModel(listModel);

                //deleted price
                listModel_price_deleted.addElement(price_item_deleted);
                list_price_deleted.setModel(listModel_price_deleted);

                //receipt list
                listModel_receipt.remove(get_row+1);


            }
        });

        //Update Button Action
        btnUpdate.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {

                int i = table.getSelectedRow();
                String amount = tableModel.getValueAt(i, 3).toString();
                int amount_int = Integer.parseInt(amount);

                if(i >= 0)
                {

                    //setting new values to the table
                    tableModel.setValueAt(name.getText(), i, 1);
                    tableModel.setValueAt(quantity.getText(), i, 2);
                    tableModel.setValueAt(price.getText(), i, 3);

                    //getting the updated values
                    String id_item = id.getText();
                    String name_item = name.getText();
                    String quantity_item = quantity.getText();
                    String price_item = price.getText();
                    int quantity_item_int = Integer.parseInt(quantity_item);
                    int price_item_updated = Integer.parseInt(price_item);

                    //Transaction History List (appending the updated values)
                    listModel.addElement("Updated Item: "+name_item+","+quantity_item_int+","+price_item_updated+" with ID "+id_item+" On date " +item_obj.getDate());
                    list.setModel(listModel);

                    //updated price list
                    if(price_item_updated>amount_int){
                        listModel_price_updated.addElement(price_item_updated-amount_int);
                    }
                    else{
                        listModel_price_updated.addElement(-(amount_int-price_item_updated));
                    }
                    list_price_updated.setModel(listModel_price_updated);

                    //receipt list
                    listModel_receipt.set(get_row+1, "Updated -- "+name_item+"-------"+quantity_item_int+"-------"+price_item_updated+"-------"+item_obj.getDate());


                }
                else{
                    System.out.println("Update Error");
                }
                //System.out.println(amount);
            }
        });

        //Add Button Action
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name_item = name.getText();
                String quantity_item = quantity.getText();
                String price_item = price.getText();

                int quantity_item_int = Integer.parseInt(quantity_item);
                int price_item_int = Integer.parseInt(price_item);

                //setting parameters to object variables
                item_obj = new item(name_item, quantity_item_int, price_item_int );
                item_obj.setId();
                item_obj.setName(name_item);
                item_obj.setQuantity(quantity_item_int);
                item_obj.setPrice(price_item_int);
                item_obj.setDate();

                //appending row to the table
                tableModel.addRow(new Object[]{item_obj.getId(), item_obj.getName(), item_obj.getQuantity(), item_obj.getPrice(), item_obj.getDate()});

                //Transaction History List
                listModel.addElement("Added Item: "+name_item+","+quantity_item_int+","+price_item_int+" with ID "+item_obj.getId()+ " On date "+item_obj.getDate());
                list.setModel(listModel);

                //Total Price List
                listModel_price.addElement(price_item_int);
                list_price.setModel(listModel_price);

                //Receipt List
                listModel_receipt.addElement(name_item+"-------"+quantity_item_int+"-------"+price_item_int+"-------"+item_obj.getDate());
                list_receipt.setModel(listModel_receipt);

                name.setText("");
                quantity.setText("");
                price.setText("");

            }
        });

        System.out.println("This is your inventory management system :)");
        while(true){
            System.out.println("Select an option to continue:");
            System.out.println("1: Print transaction history\n2: Display inventory GUI \n3: Exit Application");
            int choice = sc.nextInt();
            switch (choice){
                case 1 :
                    getTotalPrice();
                    break;
                case 2:
                    printList();
                    break;
                case 3:
                    System.out.println("Exiting");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Please make a valid choice. \n\n");
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
            int number = (int) listModel_price.getElementAt(x);
            sum+=number;
        }


        return sum;
    }
    //updated price method
    public static int getUpdatedPrice(){
        int sum = 0;
        for(int x = 0; x < listModel_price_updated.getSize(); x++) {
            int number = (int) listModel_price_updated.getElementAt(x);
            sum+=number;
        }

        return sum;
    }
    //deleted price method
    public static int getDeletedPrice(){
        int sum = 0;
        for(int x = 0; x < listModel_price_deleted.getSize(); x++) {
            int number = (int) listModel_price_deleted.getElementAt(x);
            sum+=number;
        }
        return sum;
    }

}
