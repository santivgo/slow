/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.slow.telas;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.Date;
import com.slow.dal.Conexao;
import java.nio.file.Files;
import java.awt.Image;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.ComboBoxModel;
import net.proteanit.sql.DbUtils;
import static com.slow.telas.TelaLogin.tipo_perfil_log;
/**
 *
 * @author sant
 */
public class TelaCadastro extends javax.swing.JFrame {

    /**
     * Creates new form TelaCadastro
     */
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    File img = null;
    String tipo_perfil_edt = null; // o tipo de perfil que estou editando
    
    public TelaCadastro() {
        initComponents();
        conexao = Conexao.conector();
        dstvAdmin();
        carregarTabela();
    }
    
    private boolean perfilAtivo(int i){
        return i == 0;
    }
    private void dstvAdmin(){ 
        if(!ehAdmin(tipo_perfil_log)){
            drop_tipoUsuario_cad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "cliente", "funcionario"}));
        }
    }
    
    private boolean ehAdmin(String perfil){
        return perfil.equals("admin");
    }
     
    private boolean ehFunc(String perfil){
        return perfil.equals("funcionario");
    }
    
    private void carregarTabela(){
        String sql = null;
        if (ehAdmin(tipo_perfil_log)){
            sql = "select * from usuario";
        }else{
            sql = "select * from usuario where perfil = 'usuario'";
        }
        
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tbl_clientes.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }
   
    
    private void preencherCampos(){
        int l = tbl_clientes.getSelectedRow();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        
        txt_id_edt.setText(tbl_clientes.getModel().getValueAt(l,0).toString());
        txt_nome_edt.setText(tbl_clientes.getModel().getValueAt(l,1).toString());
        txt_end_edt.setText(tbl_clientes.getModel().getValueAt(l,2).toString());
        txt_ctt_edt.setText(tbl_clientes.getModel().getValueAt(l,3).toString());

        try{
            dt_nasc_edt.setDate(sdf.parse(tbl_clientes.getModel().getValueAt(l,4).toString()));
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Não foi possível converter a data");
        }
        tipo_perfil_edt = tbl_clientes.getModel().getValueAt(l,5).toString();
        drop_tipoUsuario_edt.setSelectedItem(tipo_perfil_edt);
        
        
        if (tbl_clientes.getModel().getValueAt(l,6) != null){
            byte[] imageBytes = (byte[]) tbl_clientes.getModel().getValueAt(l,6);
            Image image = getToolkit().createImage(imageBytes);
            Image img = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            btn_img_edt.setIcon(new ImageIcon(img));
        }

            
        txt_cpf_edt.setText(tbl_clientes.getModel().getValueAt(l,7).toString());
        drop_ativo_edt.setSelectedItem(tbl_clientes.getModel().getValueAt(l,8));
        txt_login_edt.setText(tbl_clientes.getModel().getValueAt(l,9).toString());
        txt_senha_edt.setText(tbl_clientes.getModel().getValueAt(l,10).toString());

        
    }
    

    private void editar(){
        
        
        if (txt_login_edt.getText().isEmpty() || new String(txt_senha_edt.getPassword()).isEmpty() 
            || txt_nome_edt.getText().isEmpty() || txt_cpf_edt.getText().isEmpty()
            || txt_ctt_edt.getText().isEmpty() || dt_nasc_edt.getDateFormatString().isEmpty() || txt_id_edt.getText().isEmpty()){ 
            JOptionPane.showMessageDialog(null, "Um ou mais campos obrigatórios não estão preenchidos.");
        }else{
            String sql = "update usuario set nome = ?, endereco = ?, contato = ?, data_nascimento = ?, perfil = ?, img = ?, cpf = ?, login = ?, senha = ?, ativo = ? where id_usuario = ?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txt_nome_edt.getText());
                pst.setString(2, txt_end_edt.getText());
                pst.setString(3, txt_ctt_edt.getText());
                
                pst.setDate(4, new java.sql.Date(dt_nasc_edt.getDate().getTime()));
                pst.setString(5, drop_tipoUsuario_edt.getSelectedItem().toString());
                if (img == null){
                    pst.setBytes(6, null);
                }else{
                    pst.setBytes(6, Files.readAllBytes(img.toPath()));
                }
                
                pst.setString(7, txt_cpf_edt.getText());
                pst.setString(8, txt_login_edt.getText());
                pst.setString(9, new String(txt_senha_edt.getPassword()));
                pst.setBoolean(10, perfilAtivo(drop_ativo_edt.getSelectedIndex()));
                pst.setString(11, txt_id_edt.getText());


                int adicionado = pst.executeUpdate();
                
                if (adicionado > 0){
                    JOptionPane.showMessageDialog(rootPane, "Atualizado com sucesso!");        
                    carregarTabela();

                }else{
                    JOptionPane.showMessageDialog(rootPane, "Não foi possível editar usuário");
                }

                txt_end_edt.setText(null);
                drop_tipoUsuario_edt.setSelectedIndex(0);
                txt_login_edt.setText(null);
                txt_senha_edt.setText(null);
                txt_nome_edt.setText(null);
                txt_cpf_edt.setText(null);
                txt_ctt_edt.setText(null);
                txt_id_edt.setText(null);
                dt_nasc_edt.setDate(null);
                btn_img.setIcon(null);


                
            }catch(java.sql.SQLIntegrityConstraintViolationException e1){
                JOptionPane.showMessageDialog(rootPane, "Um mesmo login/id já está cadastrado no sistema");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e);
            }
            
        }
        
    }
    
    private void deletar(){
        String sql = "delete from usuario where id_usuario = ?";
        int l = tbl_clientes.getSelectedRow();
        String nomeOficial = tbl_clientes.getModel().getValueAt(l,1).toString();

        if (txt_login_edt.getText().isEmpty() || new String(txt_senha_edt.getPassword()).isEmpty() 
            || txt_nome_edt.getText().isEmpty() || txt_cpf_edt.getText().isEmpty()
            || txt_ctt_edt.getText().isEmpty() || dt_nasc_edt.getDateFormatString().isEmpty()){
            JOptionPane.showMessageDialog(null, "Um ou mais campos obrigatórios não estão preenchidos.");
        }else{
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txt_id_edt.getText());
            int opc = JOptionPane.showConfirmDialog(rootPane, "Deseja mesmo apagar " + nomeOficial + "?", "Deletar", JOptionPane.YES_NO_OPTION);
                if (opc == JOptionPane.YES_OPTION){
                    if (ehFunc(tipo_perfil_log) && ehAdmin(tipo_perfil_edt)){
                        JOptionPane.showMessageDialog(null, "Funcionário não pode apagar admin!");
                    }else{
                        int removido = pst.executeUpdate();
                        if (removido > 0){
                            JOptionPane.showMessageDialog(null, "Deletado com sucesso!");
                        }else{
                            JOptionPane.showMessageDialog(null, "Não foi possível deletar o contato");
                        }
                    
                    }
                    txt_end_edt.setText(null);
                    drop_tipoUsuario_cad.setSelectedIndex(0);
                    drop_ativo_edt.setSelectedIndex(0);
                    txt_login_edt.setText(null);
                    txt_senha_edt.setText(null);
                    txt_nome_edt.setText(null);
                    txt_cpf_edt.setText(null);
                    txt_ctt_edt.setText(null);
                    dt_nasc_edt.setDate(null);
                    txt_id_edt.setText(null);

                    btn_img.setIcon(null);

                }
           
            

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
}
    }

    private void cadastrar(){
        
        if (txt_login_cad.getText().isEmpty() || new String(txt_senha_cad.getPassword()).isEmpty() 
            || txt_nome_cad.getText().isEmpty() || txt_cpf_cad.getText().isEmpty()
            || txt_ctt_cad.getText().isEmpty() || dt_nasc_cad.getDateFormatString().isEmpty()){
            JOptionPane.showMessageDialog(null, "Um ou mais campos obrigatórios não estão preenchidos.");
        }else{
            String sql = "insert into usuario(nome, endereco, contato, data_nascimento, perfil, img, cpf, login, senha) values (?,?,?,?,?,?,?,?,?)";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txt_nome_cad.getText());
                pst.setString(2, txt_end_cad.getText());
                pst.setString(3, txt_ctt_cad.getText());
                
                pst.setDate(4, new java.sql.Date(dt_nasc_cad.getDate().getTime()));
                pst.setString(5, drop_tipoUsuario_cad.getSelectedItem().toString());
                if (img == null){
                    pst.setBytes(6, null);
                }else{
                    pst.setBytes(6, Files.readAllBytes(img.toPath()));
                }

                
                pst.setString(7, txt_cpf_cad.getText());
                pst.setString(8, txt_login_cad.getText());
                pst.setString(9, new String(txt_senha_cad.getPassword()));
                
                int adicionado = pst.executeUpdate();
                
                if (adicionado > 0){
                    JOptionPane.showMessageDialog(rootPane, "Adicionado com sucesso!");
                    
                    
                }else{
                    JOptionPane.showMessageDialog(rootPane, "Não foi possível cadastrar usuário");

                }
                    txt_end_cad.setText(null);
                    drop_tipoUsuario_cad.setSelectedIndex(0);
                    txt_login_cad.setText(null);
                    txt_senha_cad.setText(null);
                    txt_nome_cad.setText(null);
                    txt_cpf_cad.setText(null);
                    txt_ctt_cad.setText(null);
                    dt_nasc_cad.setDate(null);
                    btn_img.setIcon(null);


                
            }catch(java.sql.SQLIntegrityConstraintViolationException e1){
                JOptionPane.showMessageDialog(rootPane, "O usuário já está cadastrado no sistema");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e);
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        btn_img = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        dt_nasc_cad = new com.toedter.calendar.JDateChooser();
        drop_tipoUsuario_cad = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        txt_nome_cad = new javax.swing.JTextField();
        txt_end_cad = new javax.swing.JTextField();
        txt_login_cad = new javax.swing.JTextField();
        txt_ctt_cad = new javax.swing.JFormattedTextField();
        txt_cpf_cad = new javax.swing.JFormattedTextField();
        txt_senha_cad = new javax.swing.JPasswordField();
        jLabel10 = new javax.swing.JLabel();
        btn_cadastrar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_clientes = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        btn_pesquisar_cli = new javax.swing.JButton();
        txt_pesquisar_cli = new javax.swing.JTextField();
        btn_img_edt = new javax.swing.JButton();
        txt_nome_edt = new javax.swing.JTextField();
        txt_end_edt = new javax.swing.JTextField();
        txt_ctt_edt = new javax.swing.JFormattedTextField();
        txt_cpf_edt = new javax.swing.JFormattedTextField();
        dt_nasc_edt = new com.toedter.calendar.JDateChooser();
        txt_login_edt = new javax.swing.JTextField();
        txt_senha_edt = new javax.swing.JPasswordField();
        jLabel16 = new javax.swing.JLabel();
        txt_id_edt = new javax.swing.JTextField();
        drop_tipoUsuario_edt = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        drop_ativo_edt = new javax.swing.JComboBox<>();
        btn_editar_usr = new javax.swing.JButton();
        btn_deletar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(860, 460));
        setResizable(false);

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(860, 460));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(860, 460));
        jTabbedPane1.setRequestFocusEnabled(false);

        jPanel1.setPreferredSize(new java.awt.Dimension(860, 460));

        btn_img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/slow/icones/plus.png"))); // NOI18N
        btn_img.setMaximumSize(new java.awt.Dimension(100, 100));
        btn_img.setMinimumSize(new java.awt.Dimension(100, 100));
        btn_img.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_imgActionPerformed(evt);
            }
        });

        jLabel1.setText("Imagem");

        jLabel2.setText("Nome*");

        jLabel3.setText("Endereco");

        jLabel4.setText("Contato*");

        jLabel5.setText("Login*");

        jLabel6.setText("Data de Nascimento*");

        jLabel7.setText("Tipo de Usuário*");

        jLabel8.setText("CPF*");

        dt_nasc_cad.setDateFormatString("dd/MM/yyyy");

        drop_tipoUsuario_cad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "cliente", "funcionario", "admin" }));
        drop_tipoUsuario_cad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drop_tipoUsuario_cadActionPerformed(evt);
            }
        });

        jLabel9.setText("Senha*");

        try {
            txt_ctt_cad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) # ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_ctt_cad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_ctt_cadActionPerformed(evt);
            }
        });

        try {
            txt_cpf_cad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel10.setText("Outras Informações");

        btn_cadastrar.setText("Cadastrar");
        btn_cadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cadastrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(332, 332, 332)
                .addComponent(btn_cadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(445, 445, 445))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(jLabel1)
                                    .addComponent(btn_img, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt_senha_cad, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txt_login_cad, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4))
                                .addGap(21, 21, 21)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txt_ctt_cad, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, Short.MAX_VALUE))
                                    .addComponent(txt_nome_cad)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txt_end_cad, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txt_cpf_cad, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(drop_tipoUsuario_cad, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dt_nasc_cad, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(81, 81, 81))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_img, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txt_login_cad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txt_senha_cad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_nome_cad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txt_cpf_cad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txt_end_cad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(drop_tipoUsuario_cad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(txt_ctt_cad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6))
                    .addComponent(dt_nasc_cad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btn_cadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab1", jPanel1);

        tbl_clientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Endereco", "Data de Nascimento"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_clientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_clientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_clientes);

        jLabel11.setText("Nome*");

        jLabel12.setText("Endereco");

        jLabel13.setText("Contato*");

        jLabel14.setText("CPF*");

        jLabel15.setText("Login*");

        jLabel17.setText("Senha*");

        jLabel19.setText("Data de Nascimento*");

        btn_pesquisar_cli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/slow/icones/lupa.png"))); // NOI18N
        btn_pesquisar_cli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_pesquisar_cli_cliMouseClicked(evt);
            }
        });

        txt_pesquisar_cli.setText("ainda nao fiz");
        txt_pesquisar_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_pesquisar_cliActionPerformed(evt);
            }
        });
        txt_pesquisar_cli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_pesquisar_cliKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_pesquisar_cliKeyTyped(evt);
            }
        });

        btn_img_edt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/slow/icones/plus.png"))); // NOI18N
        btn_img_edt.setMaximumSize(new java.awt.Dimension(100, 100));
        btn_img_edt.setMinimumSize(new java.awt.Dimension(100, 100));
        btn_img_edt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_img_edtActionPerformed(evt);
            }
        });

        try {
            txt_ctt_edt.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) # ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_ctt_edt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_ctt_edtActionPerformed(evt);
            }
        });

        try {
            txt_cpf_edt.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        dt_nasc_edt.setDateFormatString("dd/MM/yyyy");

        txt_login_edt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_login_edtActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Liberation Sans", 1, 13)); // NOI18N
        jLabel16.setText("ID*");

        txt_id_edt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_id_edtActionPerformed(evt);
            }
        });

        drop_tipoUsuario_edt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "cliente", "funcionario", "admin" }));
        drop_tipoUsuario_edt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drop_tipoUsuario_edtActionPerformed(evt);
            }
        });

        jLabel18.setText("Tipo de Usuário*");

        jLabel20.setText("Status");

        drop_ativo_edt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ativo", "inativo" }));

        btn_editar_usr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/slow/icones/edit.png"))); // NOI18N
        btn_editar_usr.setText("Editar Contato");
        btn_editar_usr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editar_usrActionPerformed(evt);
            }
        });

        btn_deletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/slow/icones/delete.png"))); // NOI18N
        btn_deletar.setText("Deletar");
        btn_deletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deletarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(452, 452, 452)
                                .addComponent(txt_pesquisar_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btn_pesquisar_cli)))
                        .addContainerGap(119, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addGap(18, 18, 18)
                                        .addComponent(txt_end_edt, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txt_nome_edt, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addGap(18, 18, 18)
                                        .addComponent(txt_id_edt)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txt_cpf_edt, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txt_senha_edt, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addGap(18, 18, 18)
                                        .addComponent(txt_login_edt, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addGap(18, 18, 18)
                                .addComponent(dt_nasc_edt, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(drop_ativo_edt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(18, 18, 18)
                                .addComponent(txt_ctt_edt, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(drop_tipoUsuario_edt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_img_edt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(155, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(398, 398, 398)
                        .addComponent(btn_editar_usr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_deletar)
                        .addGap(34, 34, 34))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_pesquisar_cli, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_pesquisar_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(txt_login_edt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)
                            .addComponent(txt_id_edt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(txt_senha_edt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(txt_nome_edt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txt_cpf_edt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(txt_end_edt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(drop_tipoUsuario_edt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(drop_ativo_edt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btn_img_edt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txt_ctt_edt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dt_nasc_edt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_editar_usr)
                    .addComponent(btn_deletar))
                .addGap(35, 35, 35))
        );

        jTabbedPane1.addTab("tab2", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(870, 490));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_imgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_imgActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);
        File img = fc.getSelectedFile();
        if (img != null){
            try{
                btn_img.setIcon(new ImageIcon(ImageIO.read(img)));
            }catch(Exception e){
                System.out.println(e);
            }
        }
    }//GEN-LAST:event_btn_imgActionPerformed

    private void btn_cadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cadastrarActionPerformed
        cadastrar();
    // TODO add your handling code here:
    }//GEN-LAST:event_btn_cadastrarActionPerformed

    private void txt_ctt_cadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_ctt_cadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_ctt_cadActionPerformed

    private void btn_pesquisar_cli_cliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pesquisar_cli_cliMouseClicked
        // TODO add your handling code here:
        //pesquisar();
    }//GEN-LAST:event_btn_pesquisar_cli_cliMouseClicked

    private void txt_pesquisar_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_pesquisar_cliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_pesquisar_cliActionPerformed

    private void txt_pesquisar_cliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_pesquisar_cliKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_pesquisar_cliKeyTyped

    private void btn_img_edtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_img_edtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_img_edtActionPerformed

    private void txt_ctt_edtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_ctt_edtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_ctt_edtActionPerformed

    private void txt_login_edtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_login_edtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_login_edtActionPerformed

    private void drop_tipoUsuario_cadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drop_tipoUsuario_cadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_drop_tipoUsuario_cadActionPerformed

    private void txt_pesquisar_cliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_pesquisar_cliKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_pesquisar_cliKeyReleased

    private void txt_id_edtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_id_edtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_id_edtActionPerformed

    private void drop_tipoUsuario_edtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drop_tipoUsuario_edtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_drop_tipoUsuario_edtActionPerformed

    private void tbl_clientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_clientesMouseClicked
        // TODO add your handling code here:
        preencherCampos();
    }//GEN-LAST:event_tbl_clientesMouseClicked

    private void btn_editar_usrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editar_usrActionPerformed
        // TODO add your handling code here:
        txt_id_edt.setEnabled(false);
        editar();
    }//GEN-LAST:event_btn_editar_usrActionPerformed

    private void btn_deletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deletarActionPerformed
        // TODO add your handling code here:
        deletar();
    }//GEN-LAST:event_btn_deletarActionPerformed

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
            java.util.logging.Logger.getLogger(TelaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaCadastro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cadastrar;
    private javax.swing.JButton btn_deletar;
    private javax.swing.JButton btn_editar_usr;
    private javax.swing.JButton btn_img;
    private javax.swing.JButton btn_img_edt;
    private javax.swing.JButton btn_pesquisar_cli;
    private javax.swing.JComboBox<String> drop_ativo_edt;
    private javax.swing.JComboBox<String> drop_tipoUsuario_cad;
    private javax.swing.JComboBox<String> drop_tipoUsuario_edt;
    private com.toedter.calendar.JDateChooser dt_nasc_cad;
    private com.toedter.calendar.JDateChooser dt_nasc_edt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tbl_clientes;
    private javax.swing.JFormattedTextField txt_cpf_cad;
    private javax.swing.JFormattedTextField txt_cpf_edt;
    private javax.swing.JFormattedTextField txt_ctt_cad;
    private javax.swing.JFormattedTextField txt_ctt_edt;
    private javax.swing.JTextField txt_end_cad;
    private javax.swing.JTextField txt_end_edt;
    private javax.swing.JTextField txt_id_edt;
    private javax.swing.JTextField txt_login_cad;
    private javax.swing.JTextField txt_login_edt;
    private javax.swing.JTextField txt_nome_cad;
    private javax.swing.JTextField txt_nome_edt;
    private javax.swing.JTextField txt_pesquisar_cli;
    private javax.swing.JPasswordField txt_senha_cad;
    private javax.swing.JPasswordField txt_senha_edt;
    // End of variables declaration//GEN-END:variables
}
