package com.Ronit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class Main {

    //ONLY STATICS HERE//

    static Scanner sc = new Scanner(System.in);
    static DefaultTableModel tableModel = new DefaultTableModel();

    static JTable table = new JTable(tableModel);
    static JFrame f = new JFrame();

    //Transaction List
    static JList list = new JList();
    static DefaultListModel listModel = new DefaultListModel();

    //Price List
    static JList list_price = new JList();
    static DefaultListModel listModel_price = new DefaultListModel();

    //Update list Price
    static JList list_price_updated = new JList();
    static DefaultListModel listModel_price_updated = new DefaultListModel();

    //Receipt List
    static JList list_receipt = new JList();
    static DefaultListModel listModel_receipt = new DefaultListModel();

    //Creating an object of class
    static item item_obj;

    static int m; //total price
    static int u; //updated price

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



        //Initializing Text Fields
        JTextField id = new JTextField();
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
        id.setBounds(20, 220, 100, 25);
        name.setBounds(20, 220, 100, 25);
        quantity.setBounds(20, 250, 100, 25);
        price.setBounds(20, 280, 100, 25);

        //setting bounds and visibility to the lists
        list.setBounds(400, 210, 500, 100);
        list.setVisible(true);
        list_receipt.setBounds(400, 400, 300, 100);
        list_receipt.setVisible(false);
        total_price.setBounds(400, 510, 300, 25);
        total_price.setVisible(false);

        //setting bounds to the buttons
        btnAdd.setBounds(150, 220, 100, 25);
        btnUpdate.setBounds(150, 265, 100, 25);
        btnDelete.setBounds(150, 310, 100, 25);
        btnReceipt.setBounds(150, 355, 100, 25);



        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(0, 0, 880, 200);

        //adding all components to main frame
        f.setLayout(null);
        f.add(pane);


        f.add(name);
        f.add(quantity);
        f.add(price);

        f.add(btnAdd);
        f.add(btnDelete);
        f.add(btnUpdate);
        f.add(btnReceipt);

        f.add(list);
        f.add(list_receipt);
        f.add(total_price);



        listModel_receipt.addElement("ITEM-------QUANTITY-------PRICE-------DATE");
        list_receipt.setModel(listModel_receipt);


        //mouse on click listener, gives parameters of clicked row
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int i = table.getSelectedRow();
                id.setText(tableModel.getValueAt(i, 0).toString());
                name.setText(tableModel.getValueAt(i, 1).toString());
                quantity.setText(tableModel.getValueAt(i, 2).toString());
                price.setText(tableModel.getValueAt(i, 3).toString());
            }
            });

        //Receipt Button Action
        btnReceipt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                list_receipt.setVisible(true);
                total_price.setVisible(true);
                m = getTotalPrice();
                u = getUpdatedPrice();
                total_price.setText("Total amount: " + (m+u));

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
                int price_item_int = Integer.parseInt(price_item);
                System.out.println(name_item+","+quantity_item_int+","+price_item_int);

                //Transaction History List
                listModel.addElement("Removed Item: "+name_item+","+quantity_item_int+","+price_item_int+"On date "+item_obj.getDate()+ " with ID"+id_item);
                list.setModel(listModel);

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


                    if(price_item_updated>amount_int){
                        listModel_price_updated.addElement(price_item_updated-amount_int);
                    }
                    else{
                        listModel_price_updated.addElement(-(amount_int-price_item_updated));
                    }
                    list_price_updated.setModel(listModel_price_updated);


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

    public static int getUpdatedPrice(){
        int sum = 0;
        for(int x = 0; x < listModel_price_updated.getSize(); x++) {
            int number = (int) listModel_price_updated.getElementAt(x);
            sum+=number;
        }

        return sum;
    }

}
