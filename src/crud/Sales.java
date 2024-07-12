/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package crud;

import connection.MyDatabase;
import connection.MyDatabase;
import java.awt.Component;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lid2jvl
 */
public class Sales extends javax.swing.JDialog {

    /**
     * Creates new form sales
     */
    public Sales(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    MyDatabase db = new MyDatabase();
    
    private void searchClient(){
        if(db.getConnection()){
            try{
                String query = "select * from custumer where name_cus like ?";
                PreparedStatement smtp = db.conn.prepareStatement(query);
                smtp.setString(1,"%"+jTNameCustomer.getText()+"%");
                ResultSet rs = smtp.executeQuery();
                
                while(rs.next()){
                    String add1 = rs.getString("idcustumer");
                    jTCodeCustomer.setText(add1);
                    String add2 = rs.getString("name_cus");
                    jTNameCustomer.setText(add2);
                    System.out.println(rs.getString("cpf_cus"));
                    String add3 = rs.getString("cpf_cus");
                    jTCpfCustomer.setText(add3);
                }
                smtp.close();
                db.conn.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
                System.out.println("Error to searching "+e);
            }
        }
    }

    private void sale(){
        if(db.getConnection()){
            try{
                String query = "insert sales (custumer_idcustumer) values(?)";
                PreparedStatement smtp = db.conn.prepareStatement(query);
                smtp.setString(1, jTCodeCustomer.getText());
                smtp.executeUpdate();
                smtp.close();
                db.conn.close();
                
            }catch(SQLException error){
                JOptionPane.showMessageDialog(null, "EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                JOptionPane.showMessageDialog(null, "Error of Save" + error.toString());
            }
        }
    }
    
    private void searchProduct(){
        if(db.getConnection()){
            try{
                String query = "select * from product where id_product like ?";
                PreparedStatement smtp = db.conn.prepareStatement(query);
                smtp.setString(1,"%"+jTCodeProduct.getText()+"%");
                ResultSet rs = smtp.executeQuery();
                
                while(rs.next()){
                    System.out.println(rs.getString("description_product"));
                    String add1 = rs.getString("id_product");
                    jTCodeProduct.setText(add1);
                    String add2 = rs.getString("description_product");
                    jTProductDescription.setText(rs.getString("description_product"));
                    String add3 = rs.getString("value_product");
                    jTProductPrice.setText(add3);
                }
                smtp.close();
                db.conn.close();
            }catch(SQLException error){
                JOptionPane.showMessageDialog(null, "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
                JOptionPane.showMessageDialog(null, "Error to search product" + error.toString());
            }
        }
    }
    
    private void id_sale(){
        if(db.getConnection()){
            try{
                String query = "select max(id_sales) as id_sales from sales";
                PreparedStatement smtp = db.conn.prepareStatement(query);
                ResultSet rs = smtp.executeQuery();
                while(rs.next()){
                    String add1 = rs.getString("id_sales");
                    jTInvoiceProduct.setText(add1);
                }
                
                smtp.close();
                db.conn.close();
            }catch(SQLException error){
                JOptionPane.showMessageDialog(null, "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
                JOptionPane.showMessageDialog(null, "Error to Save" + error.toString());
            }
        }
    }
    
    private void calculateProduct(){
        float amount = Float.valueOf(jTProductAmount.getText());
        float value = Float.valueOf(jTProductPrice.getText());
        float total = amount * value;
        jTProductTotal.setText(String.valueOf(total));
    }
    
    private void add_items_sales(){
        if(db.getConnection()){
            try{
                String query = "insert product_has_sales (id_product, id_sales, amount, unit_value, total) values (?,?,?,?,?)";
                PreparedStatement smtp = db.conn.prepareStatement(query);
                smtp.setString(1, jTCodeProduct.getText());
                smtp.setString(2, jTInvoiceProduct.getText());
                smtp.setString(3, jTProductAmount.getText());
                smtp.setString(4, jTProductPrice.getText());
                smtp.setString(5, jTProductTotal.getText());
                smtp.setString(5, jTProductTotal.getText());                

                smtp.executeUpdate();
                db.conn.close();
            }
            catch(SQLException error){
                JOptionPane.showMessageDialog(null, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                JOptionPane.showMessageDialog(null, "Save error"+error.toString());
            }
        }
    }
    
    private void consulting_invoice(){
        if(db.getConnection()){
            try{
                String query = "select phs.id_product,"
                        +"p.description_product,"
                        +"phs.unit_value,"
                        +"phs.amount,"
                        +"phs.total "
                        +"FROM product_has_sales AS phs "
                        +"INNER JOIN product AS p "
                        +"ON phs.id_product = p.id_product "
                        +"WHERE phs.id_sales = ?";
                
                PreparedStatement smtp = db.conn.prepareStatement(query);
                smtp.setString(1, jTInvoiceProduct.getText());
                ResultSet rs;
                rs = smtp.executeQuery();
                DefaultTableModel model = (DefaultTableModel)jTableSales.getModel();
                model.setNumRows(0);
                
                while(rs.next()){
                    model.addRow(new Object[]{
                        rs.getString("id_product"),
                        rs.getString("description_product"),
                        rs.getString("unit_value"),
                        rs.getString("amount"),
                        rs.getString("total"),
                    });
                }
                
            }catch(SQLException error){
                JOptionPane.showMessageDialog(null, "Error of search"+error.toString());
            }
        }
    }
    
    private void sumnf(){
        if(db.getConnection()){
            try{
                String query = "SELECT sum(total) as total FROM product_has_sales "
                              +"WHERE id_sales = ?";
                
                PreparedStatement smtp = db.conn.prepareStatement(query);
                smtp.setString(1, jTInvoiceProduct.getText());                
                ResultSet rs;
                rs = smtp.executeQuery();
                
                if(rs.next()){
                    String add1 = rs.getString("total");
                    jTSum.setText(add1);
                }
                
            }catch(SQLException error){
                JOptionPane.showMessageDialog(null, "Error of connection"+error.toString());
            }
        }
    }
    
    private static void clearFields(JPanel jPanel){
        Component[] components = jPanel.getComponents();
        for(Component component : components){
            if(component instanceof JTextField){
                JTextField fieldsTF = (JTextField)component;
                fieldsTF.setText("");
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTNameCustomer = new javax.swing.JTextField();
        jTCodeCustomer = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTCpfCustomer = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableSales = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTSum = new javax.swing.JTextField();
        jBEndSale = new javax.swing.JButton();
        jBNewSale = new javax.swing.JButton();
        jBCancelSale = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTProductDescription = new javax.swing.JTextField();
        jTInvoiceProduct = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTCodeProduct = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTProductAmount = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTProductPrice = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTProductTotal = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jBAddProduct = new javax.swing.JButton();

        jScrollPane2.setViewportView(jTextPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setBounds(new java.awt.Rectangle(255, 255, 255, 1));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusCycleRoot(false);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setForeground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel14.setBackground(new java.awt.Color(0, 73, 204));

        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Customer datas");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setText("Code");

        jTNameCustomer.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTNameCustomerCaretUpdate(evt);
            }
        });
        jTNameCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTNameCustomerActionPerformed(evt);
            }
        });

        jTCodeCustomer.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTCodeCustomerCaretUpdate(evt);
            }
        });
        jTCodeCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTCodeCustomerActionPerformed(evt);
            }
        });

        jLabel3.setText("Name:");

        jLabel6.setText("Cpf");

        jTCpfCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTCpfCustomerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jTCodeCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTCpfCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTNameCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTCodeCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTCpfCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTNameCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(48, 56, 72));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("SALES MODULE");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(284, 284, 284)
                .addComponent(jLabel1)
                .addContainerGap(308, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jTableSales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Description", "Value", "Amount", "Total"
            }
        ));
        jScrollPane1.setViewportView(jTableSales);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel11.setText("TOTAL VALUE");

        jTSum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTSumActionPerformed(evt);
            }
        });

        jBEndSale.setText("End");
        jBEndSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEndSaleActionPerformed(evt);
            }
        });

        jBNewSale.setText("New");
        jBNewSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBNewSaleActionPerformed(evt);
            }
        });

        jBCancelSale.setText("Cancel");
        jBCancelSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCancelSaleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jBNewSale, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(jBCancelSale, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBEndSale, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(jTSum, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTSum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBEndSale)
                    .addComponent(jBNewSale)
                    .addComponent(jBCancelSale))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel15.setBackground(new java.awt.Color(0, 73, 204));

        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Invoice");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setText("Invoice");

        jTProductDescription.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTProductDescriptionCaretUpdate(evt);
            }
        });
        jTProductDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTProductDescriptionActionPerformed(evt);
            }
        });

        jTInvoiceProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTInvoiceProductActionPerformed(evt);
            }
        });

        jLabel5.setText("Product");

        jTCodeProduct.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTCodeProductCaretUpdate(evt);
            }
        });
        jTCodeProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTCodeProductActionPerformed(evt);
            }
        });

        jLabel7.setText("Product code");

        jTProductAmount.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTProductAmountCaretUpdate(evt);
            }
        });
        jTProductAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTProductAmountActionPerformed(evt);
            }
        });

        jLabel8.setText("Price");

        jTProductPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTProductPriceActionPerformed(evt);
            }
        });

        jLabel9.setText("Amount");

        jTProductTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTProductTotalActionPerformed(evt);
            }
        });

        jLabel10.setText("Total");

        jBAddProduct.setText("Add");
        jBAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAddProductActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTProductDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addComponent(jTInvoiceProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(6, 6, 6))
                                .addComponent(jTCodeProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(75, 75, 75)
                        .addComponent(jLabel9))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10)
                                .addComponent(jTProductTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jBAddProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jTProductPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jTProductAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTInvoiceProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addGap(28, 28, 28)))
                    .addComponent(jTCodeProduct, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTProductDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8))
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTProductPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTProductAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTProductTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBAddProduct))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(89, 89, 89)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTNameCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTNameCustomerActionPerformed
        searchClient();
    }//GEN-LAST:event_jTNameCustomerActionPerformed

    private void jTCodeCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTCodeCustomerActionPerformed
        searchClient();
    }//GEN-LAST:event_jTCodeCustomerActionPerformed

    private void jTProductDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTProductDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTProductDescriptionActionPerformed

    private void jTInvoiceProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTInvoiceProductActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTInvoiceProductActionPerformed

    private void jTSumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTSumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTSumActionPerformed

    private void jTCodeProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTCodeProductActionPerformed
        searchProduct();
    }//GEN-LAST:event_jTCodeProductActionPerformed

    private void jTProductAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTProductAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTProductAmountActionPerformed

    private void jTProductPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTProductPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTProductPriceActionPerformed

    private void jTProductTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTProductTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTProductTotalActionPerformed

    private void jBAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAddProductActionPerformed
        add_items_sales();
        consulting_invoice();
        sumnf();
    }//GEN-LAST:event_jBAddProductActionPerformed

    private void jTCpfCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTCpfCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTCpfCustomerActionPerformed

    private void jBEndSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEndSaleActionPerformed
        Payment pgto = new Payment(null, true);
        clearFields(jPanel1);
        pgto.setVisible(true);
    }//GEN-LAST:event_jBEndSaleActionPerformed

    private void jBNewSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBNewSaleActionPerformed
        sale();
        id_sale();
    }//GEN-LAST:event_jBNewSaleActionPerformed

    private void jBCancelSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelSaleActionPerformed
       
    }//GEN-LAST:event_jBCancelSaleActionPerformed

    private void jTProductDescriptionCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTProductDescriptionCaretUpdate
    }//GEN-LAST:event_jTProductDescriptionCaretUpdate

    private void jTNameCustomerCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTNameCustomerCaretUpdate
        
    }//GEN-LAST:event_jTNameCustomerCaretUpdate

    private void jTCodeProductCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTCodeProductCaretUpdate
    }//GEN-LAST:event_jTCodeProductCaretUpdate

    private void jTCodeCustomerCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTCodeCustomerCaretUpdate
        // TODO add your handling code here:
    }//GEN-LAST:event_jTCodeCustomerCaretUpdate

    private void jTProductAmountCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTProductAmountCaretUpdate
        calculateProduct();
    }//GEN-LAST:event_jTProductAmountCaretUpdate

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Sales dialog = new Sales(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAddProduct;
    private javax.swing.JButton jBCancelSale;
    private javax.swing.JButton jBEndSale;
    private javax.swing.JButton jBNewSale;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTCodeCustomer;
    private javax.swing.JTextField jTCodeProduct;
    private javax.swing.JTextField jTCpfCustomer;
    private javax.swing.JTextField jTInvoiceProduct;
    private javax.swing.JTextField jTNameCustomer;
    private javax.swing.JTextField jTProductAmount;
    private javax.swing.JTextField jTProductDescription;
    private javax.swing.JTextField jTProductPrice;
    private javax.swing.JTextField jTProductTotal;
    private javax.swing.JTextField jTSum;
    private javax.swing.JTable jTableSales;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
