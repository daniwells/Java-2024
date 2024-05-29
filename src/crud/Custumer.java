/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package crud;

//import connection.MyDatabase;
//import javax.swing.JOptionPane;
// ctrl + shift + i

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
public class Custumer extends javax.swing.JDialog {

    /**
     * Creates new form database_connection
     */
    public Custumer(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        setResizable(false);
        setLocationRelativeTo(null);
        searchClient();
        initTableListener();
    }
    
    MyDatabase db = new MyDatabase();
    
    private void registerCustumer(){
        if(db.getConnection()){
            try{
                String query = "insert custumer(name_cus, cpf_cus, "+
                               "date_birth_cus, city_cus, state_cus,"+
                               "street_cus, sex_cus, home_cus, defic_cus)"+
                                "values(?,?,?,?,?,?,?,?,?)";
                PreparedStatement smtp = db.conn.prepareStatement(query);
                smtp.setString(1, jTname.getText());
                smtp.setString(2, jTcpf.getText());
                smtp.setString(3, jTbirth.getText());
                smtp.setString(4, jTcity.getText());
                smtp.setString(5, jTstate.getText());
                smtp.setString(6, jTstreet.getText());
                
                String selectedValue = jCsex.getSelectedItem().toString();
                smtp.setString(7, selectedValue);
                
                if(jChome.isSelected()){
                    smtp.setString(8, "Active");
                }else{
                    smtp.setString(8, "Inactive");
                }
                
                if(jCdeficity.isSelected()){
                    smtp.setString(9, "Yes");
                }else{
                    smtp.setString(9, "No");
                }
                smtp.executeUpdate();
                JOptionPane.showMessageDialog(null, "DATAS WAS REGISTERED");
                smtp.close();
                db.conn.close();
            }catch(SQLException error){
                JOptionPane.showMessageDialog(null, "Insert error in the database!"+error.toString());
            }
        }
    }
    
    private void searchClient(){
        if(db.getConnection()){
            try{
                String query = "select * from custumer where name_cus like ?";
                PreparedStatement smtp = db.conn.prepareStatement(query);
                smtp.setString(1,"%"+jTsearch.getText()+"%");
                ResultSet rs = smtp.executeQuery();
                DefaultTableModel table = (DefaultTableModel) jTableCustumer.getModel();
                table.setNumRows(0);
                while(rs.next()){
                    table.addRow(new Object[] {
                        rs.getString("idcustumer"),
                        rs.getString("name_cus"),
                        rs.getString("cpf_cus"),
                        rs.getString("date_birth_cus"),
                        rs.getString("state_cus"),
                        rs.getString("city_cus"),
                        rs.getString("street_cus"),
                        rs.getString("sex_cus")
                    });                                   
                }
                smtp.close();
                db.conn.close();
            }catch(SQLException e){
                System.out.println("Error to searching "+e);
            }
        }
    }
    
    private void initTableListener(){
        jTableCustumer.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                int selectedRow = jTableCustumer.getSelectedRow();
                if(selectedRow != -1){
                    jLid.setText(jTableCustumer.getValueAt(selectedRow, 0).toString());
                    jTname.setText(jTableCustumer.getValueAt(selectedRow, 1).toString());
                    jTcpf.setText(jTableCustumer.getValueAt(selectedRow, 2).toString());
                    jTbirth.setText(jTableCustumer.getValueAt(selectedRow, 3).toString());
                    jTcity.setText(jTableCustumer.getValueAt(selectedRow, 4).toString());
                    jTstate.setText(jTableCustumer.getValueAt(selectedRow, 5).toString());
                    jTstreet.setText(jTableCustumer.getValueAt(selectedRow, 6).toString());
                    jCsex.setSelectedItem(jTableCustumer.getValueAt(selectedRow, 7).toString());  
                }
            }
        });
    }
    
    private void changeCostumerDatas(){
        if(db.getConnection()){
            try{
                String query = "update custumer set name_cus=?, cpf_cus=?, date_birth_cus=?, "+
                                "state_cus=?, city_cus=?, street_cus=?, sex_cus=? where idcustumer = ? ";
                
                PreparedStatement changeDatas = db.conn.prepareStatement(query);
                changeDatas.setString(1, jTname.getText());
                changeDatas.setString(2, jTcpf.getText());
                changeDatas.setString(3, jTbirth.getText());
                changeDatas.setString(5, jTstate.getText());
                changeDatas.setString(4, jTcity.getText());
                changeDatas.setString(6, jTstreet.getText());
                changeDatas.setString(7, jCsex.getSelectedItem().toString());
                changeDatas.setString(8, jLid.getText());
                
                changeDatas.executeUpdate();
                JOptionPane.showMessageDialog(null, "CHANDED DATAS!");
                
                changeDatas.close();
                db.conn.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "ERROR IN THE SQL"+e.getMessage());
            }
        }
    }
    
    private void remove_costumer(){
        if(db.getConnection()){
            try{
                String query = "DELETE FROM custumer WHERE idcustumer = ?";
                PreparedStatement remove = db.conn.prepareStatement(query);
                String index = (String)jTableCustumer.getModel().getValueAt(jTableCustumer.getSelectedRow(), 0);
                remove.setString(1, index);
                
                int choice = JOptionPane.showConfirmDialog(null, "Deseja excluir o cliente?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if(choice == JOptionPane.YES_OPTION){
                    int result = remove.executeUpdate();
                    if(result>0){
                        JOptionPane.showMessageDialog(null, "Costumer removed with success!");
                    }else{
                        JOptionPane.showMessageDialog(null, "Was cannot to remve the costumer!");
                    }
                    
                    remove.close();
                    db.conn.close();
                }
                
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Error to remove"+e.toString());
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
    
    private void clearTable(){
        DefaultTableModel table = (DefaultTableModel)jTableCustumer.getModel();
        table.setNumRows(0);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPaddress = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTstate = new javax.swing.JTextField();
        jTcity = new javax.swing.JTextField();
        jTstreet = new javax.swing.JTextField();
        jPpersonalDatas = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTname = new javax.swing.JTextField();
        jTbirth = new javax.swing.JTextField();
        jTcpf = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jCsex = new javax.swing.JComboBox<>();
        jCdeficity = new javax.swing.JCheckBox();
        jChome = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCustumer = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTsearch = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jclose = new javax.swing.JButton();
        jBedit = new javax.swing.JButton();
        jBadd = new javax.swing.JButton();
        jBremove = new javax.swing.JButton();
        jBclear = new javax.swing.JButton();
        jLid = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));

        jPanel2.setBackground(new java.awt.Color(48, 56, 72));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("REGISTER OF CUSTUMER");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(190, 190, 190)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(250, 250, 250));

        jPaddress.setBackground(new java.awt.Color(250, 250, 250));
        jPaddress.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel8.setText("State");

        jPanel11.setBackground(new java.awt.Color(0, 73, 204));

        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Address");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(jLabel13)
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel14.setText("City");

        jLabel15.setText("Street");

        jTcity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTcityActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPaddressLayout = new javax.swing.GroupLayout(jPaddress);
        jPaddress.setLayout(jPaddressLayout);
        jPaddressLayout.setHorizontalGroup(
            jPaddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPaddressLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPaddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPaddressLayout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPaddressLayout.createSequentialGroup()
                        .addGroup(jPaddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTcity, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTstreet)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPaddressLayout.createSequentialGroup()
                                .addGroup(jPaddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jTstate, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(19, 19, 19))))
        );
        jPaddressLayout.setVerticalGroup(
            jPaddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPaddressLayout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTstate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTcity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTstreet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPpersonalDatas.setBackground(new java.awt.Color(250, 250, 250));
        jPpersonalDatas.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setText("Name");

        jPanel8.setBackground(new java.awt.Color(0, 73, 204));

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Personal Datas");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(57, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(65, 65, 65))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addContainerGap())
        );

        jLabel11.setText("Date of birth");

        jLabel12.setText("CPF");

        jTbirth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTbirthActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPpersonalDatasLayout = new javax.swing.GroupLayout(jPpersonalDatas);
        jPpersonalDatas.setLayout(jPpersonalDatasLayout);
        jPpersonalDatasLayout.setHorizontalGroup(
            jPpersonalDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPpersonalDatasLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPpersonalDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPpersonalDatasLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPpersonalDatasLayout.createSequentialGroup()
                        .addGroup(jPpersonalDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTbirth, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTcpf)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPpersonalDatasLayout.createSequentialGroup()
                                .addGroup(jPpersonalDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jTname, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(19, 19, 19))))
        );
        jPpersonalDatasLayout.setVerticalGroup(
            jPpersonalDatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPpersonalDatasLayout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTbirth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTcpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(250, 250, 250));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jCsex.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Gender", "Masculine", "Feminine", "Other" }));

        jCdeficity.setText("Have some deficity?");

        jChome.setText("Have any home?");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jCdeficity)
                .addGap(18, 18, 18)
                .addComponent(jChome)
                .addGap(18, 18, 18)
                .addComponent(jCsex, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCsex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCdeficity)
                    .addComponent(jChome))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(250, 250, 250));

        jTableCustumer.setAutoCreateRowSorter(true);
        jTableCustumer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTableCustumer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"teste", "teste", "teste", "teste", "teste", "teste", null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "id", "name", "cpf", "date of birth", "state", "city", "street", "sex"
            }
        ));
        jTableCustumer.setShowGrid(true);
        jScrollPane1.setViewportView(jTableCustumer);

        jPanel4.setBackground(new java.awt.Color(250, 250, 250));

        jLabel2.setText("Search custumers");

        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jTsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(78, 78, 78))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTsearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jclose.setText("Close");

        jBedit.setBackground(new java.awt.Color(48, 56, 72));
        jBedit.setForeground(new java.awt.Color(255, 255, 255));
        jBedit.setText("Edit");
        jBedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBeditActionPerformed(evt);
            }
        });

        jBadd.setBackground(new java.awt.Color(48, 56, 72));
        jBadd.setForeground(new java.awt.Color(255, 255, 255));
        jBadd.setText("Add");
        jBadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBaddActionPerformed(evt);
            }
        });

        jBremove.setBackground(new java.awt.Color(48, 56, 72));
        jBremove.setForeground(new java.awt.Color(255, 255, 255));
        jBremove.setText("Remove");
        jBremove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBremoveActionPerformed(evt);
            }
        });

        jBclear.setBackground(new java.awt.Color(48, 56, 72));
        jBclear.setForeground(new java.awt.Color(255, 255, 255));
        jBclear.setText("Clear");
        jBclear.setBorderPainted(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jBadd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBremove)
                .addGap(12, 12, 12)
                .addComponent(jBedit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBclear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jclose)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBadd)
                    .addComponent(jBremove)
                    .addComponent(jBedit)
                    .addComponent(jBclear)
                    .addComponent(jclose))
                .addContainerGap())
        );

        jLid.setText("ID");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPpersonalDatas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPaddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLid)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPaddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPpersonalDatas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLid))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTbirthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTbirthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTbirthActionPerformed

    private void jTcityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTcityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTcityActionPerformed

    private void jBeditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBeditActionPerformed
        initTableListener();
        changeCostumerDatas();
        searchClient();
        clearFields(jPpersonalDatas);
        clearFields(jPaddress);
    }//GEN-LAST:event_jBeditActionPerformed

    private void jBaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBaddActionPerformed
        registerCustumer();
        searchClient();
        clearFields(jPpersonalDatas);
        clearFields(jPaddress);
    }//GEN-LAST:event_jBaddActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        searchClient();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jBremoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBremoveActionPerformed
        remove_costumer();
        clearFields(jPpersonalDatas);
        clearFields(jPaddress);
        clearTable();
        searchClient();
    }//GEN-LAST:event_jBremoveActionPerformed

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
            java.util.logging.Logger.getLogger(Custumer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Custumer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Custumer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Custumer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Custumer dialog = new Custumer(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jBadd;
    private javax.swing.JButton jBclear;
    private javax.swing.JButton jBedit;
    private javax.swing.JButton jBremove;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCdeficity;
    private javax.swing.JCheckBox jChome;
    private javax.swing.JComboBox<String> jCsex;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLid;
    private javax.swing.JPanel jPaddress;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPpersonalDatas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableCustumer;
    private javax.swing.JTextField jTbirth;
    private javax.swing.JTextField jTcity;
    private javax.swing.JTextField jTcpf;
    private javax.swing.JTextField jTname;
    private javax.swing.JTextField jTsearch;
    private javax.swing.JTextField jTstate;
    private javax.swing.JTextField jTstreet;
    private javax.swing.JButton jclose;
    // End of variables declaration//GEN-END:variables
}
