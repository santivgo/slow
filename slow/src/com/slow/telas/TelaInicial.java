/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.slow.telas;
import java.sql.*;
import java.awt.Image;
import java.awt.Toolkit;
import com.slow.dal.Conexao;
import static com.slow.telas.TelaLogin.img_usr;
import static com.slow.telas.TelaLogin.nome_usr;
import static com.slow.telas.TelaLogin.situacao_usr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
/**
 *
 * @author aluno
 */
public class TelaInicial extends javax.swing.JFrame {

    /**
     * Creates new form telaInicial
     */
    
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null; 
    public static String id_midia = null;
    
    public TelaInicial() {
        initComponents();
        conexao = Conexao.conector();
        organizarUsuario();
        iniciarPopular();
    }
    
    private void pesquisar(){
        
        if (txt_pesquisar_filme.getText() != null){
            String sql = "SELECT id_midia, nome, img from midia where nome LIKE ?";
            try{
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txt_pesquisar_filme.getText()+"%");
                rs = pst.executeQuery();
                popular(rs);
            }catch (Exception e){
                JOptionPane.showMessageDialog(rootPane, e);
            }
        }else{
            iniciarPopular();
        }
       
        
        
    }
    private void organizarUsuario(){
        if (img_usr != null){
            Image image = getToolkit().createImage(img_usr);
            Image img = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            img_usuario.setIcon(new ImageIcon(img));
        }
        
        usr_nome.setText(nome_usr);
        
        if (situacao_usr){
            usr_situacao.setText("SEM PENDÊNCIAS");
        }else{
            usr_situacao.setText("EM ATRASO");
        }

        

    }
    
    private int pegarTamanho(){
        String sql = "select count(id_midia) from midia";
        PreparedStatement pst = null;

        
        try{
            pst = conexao.prepareStatement(sql);
            pst.executeQuery();
            
            if (rs.next()){
                int tamanho = (int) Math.round(((double)rs.getInt(1) / 2)+0.5d);
                return tamanho;
            }else{
                return 0;
            }
            
            
        }catch (Exception e){
            return 0;
        }
        
    }
    
    private void iniciarPopular(){
        String sql = "SELECT id_midia, nome, img from midia";
        PreparedStatement pst = null;

        try{
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            popular(rs);
        }catch (Exception e){
            JOptionPane.showMessageDialog(rootPane, e);
        }
        
    }
    private void popular(ResultSet rs){
        
        jPanel1.removeAll();

        // em um jscrollpane e jpanel nao pode fixar os tamanhos se nao os botoes nao vao se espremer pra caber la
        try{
            
            
            while(rs.next()){

                byte[] imageBytes = rs.getBytes(3);
                Image image=getToolkit().createImage(imageBytes);
                
                Image img = image.getScaledInstance(100,150,Image.SCALE_SMOOTH);
                
                JLabel label = new JLabel(rs.getString(2), new ImageIcon(img), SwingConstants.LEADING);
                label.setFont(new Font("Ubuntu", Font.BOLD, 14));
                label.setHorizontalTextPosition(JLabel.CENTER);
                label.setVerticalTextPosition(JLabel.BOTTOM);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));

                JButton button = new JButton();
                button.setBackground(new Color(224,158,51));
                button.setLayout(new GridLayout(0, 1));
                button.add(label);
                button.setPreferredSize(new Dimension(150, 200));
                button.setMaximumSize(new Dimension(150, 200));
                button.setMinimumSize(new Dimension(150, 200));
                
                // é oq deixa feio
                //button.setContentAreaFilled(false);
                
                button.setActionCommand(rs.getString(1)); // guarda o id como identificador
                
                button.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e){
                        JButton botaoClicked = (JButton) e.getSource();
                        id_midia = botaoClicked.getActionCommand();
                        
                        TelaInfoMidias tim = new TelaInfoMidias();
                        tim.setVisible(true);
                        
                    }
                });


                jPanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                jPanel1.add(button);
                
            }
            jPanel1.revalidate();
            jPanel1.repaint();

        }catch (Exception e){
            JOptionPane.showMessageDialog(rootPane, e);
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
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        img_usuario = new javax.swing.JLabel();
        usr_nome = new javax.swing.JLabel();
        usr_situacao = new javax.swing.JLabel();
        btn_usr_alugueis = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        txt_pesquisar_filme = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        btn_pesquisar = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        cad_usr = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(610, 430));
        setResizable(false);

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setToolTipText("");
        jScrollPane2.setHorizontalScrollBar(null);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(10, 10));
        jScrollPane2.setViewportView(jPanel1);

        jPanel1.setBackground(new java.awt.Color(255, 197, 103));
        jPanel1.setMinimumSize(new java.awt.Dimension(750, 430));
        jPanel1.setLayout(new java.awt.GridLayout(pegarTamanho(), 4));
        jScrollPane2.setViewportView(jPanel1);

        jPanel2.setBackground(new java.awt.Color(251, 125, 168));
        jPanel2.setMinimumSize(new java.awt.Dimension(204, 430));
        jPanel2.setPreferredSize(new java.awt.Dimension(730, 430));

        img_usuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/slow/icones/default.png"))); // NOI18N
        img_usuario.setToolTipText("");

        usr_nome.setBackground(new java.awt.Color(255, 255, 255));
        usr_nome.setFont(new java.awt.Font("Open Sans Condensed", 1, 18)); // NOI18N
        usr_nome.setForeground(new java.awt.Color(255, 255, 255));
        usr_nome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        usr_nome.setText("jLabel1");

        usr_situacao.setFont(new java.awt.Font("Nimbus Sans L", 1, 18)); // NOI18N
        usr_situacao.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        usr_situacao.setText("jLabel1");

        btn_usr_alugueis.setText("Inventário");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(usr_situacao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(6, 6, 6))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(usr_nome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(img_usuario)
                .addGap(56, 56, 56))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(btn_usr_alugueis, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(img_usuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usr_nome)
                .addGap(75, 75, 75)
                .addComponent(usr_situacao)
                .addGap(39, 39, 39)
                .addComponent(btn_usr_alugueis)
                .addContainerGap(93, Short.MAX_VALUE))
        );

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Data de Adição", "Data de Lançamento", "Nome" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        txt_pesquisar_filme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_pesquisar_filmeActionPerformed(evt);
            }
        });
        txt_pesquisar_filme.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_pesquisar_filmeKeyTyped(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos os gêneros", "Ação", "Aventura", "Animação", "Comédia", "Crime", "Documentário", "Drama", "Família", "Fantasia", "História", "Thriller", "Musical", "Mistério", "Romance", "Ficção Científica", "Cinema TV", "Guerra", "Faroeste", "Ação & Aventura (seriado)", "Infântil", "Programa de TV", "Novela", "Reality Show", "Sci-fi & Fantasia (seriado)" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        btn_pesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/slow/icones/lupa.png"))); // NOI18N
        btn_pesquisar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_pesquisarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 392, Short.MAX_VALUE)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt_pesquisar_filme, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btn_pesquisar)
                .addGap(20, 20, 20))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_pesquisar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_pesquisar_filme, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(jComboBox2))
                .addGap(2, 2, 2))
        );

        jMenu1.setText("Cadastrar");

        cad_usr.setText("Usuário");
        cad_usr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cad_usrActionPerformed(evt);
            }
        });
        jMenu1.add(cad_usr);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Editar");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_pesquisar_filmeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_pesquisar_filmeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_pesquisar_filmeActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
        
        if(jComboBox2.getSelectedItem().toString().equals("Todos os gêneros")){
            iniciarPopular();
        }else{
            String sql = "SELECT m.id_midia, m.nome, m.img from midia m inner join midia_categoria mc on mc.id_midia = m.id_midia inner join categoria c on c.id_categoria = mc.id_categoria where c.nome = ?";
            
            try{
                pst = conexao.prepareStatement(sql);
                pst.setString(1, jComboBox2.getSelectedItem().toString());
                rs = pst.executeQuery();
                popular(rs);
            }catch(Exception e){
                JOptionPane.showMessageDialog(rootPane, e);
            }
                
                
            
        }
        
      
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void btn_pesquisarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pesquisarMouseClicked
        // TODO add your handling code here:
        pesquisar();

    }//GEN-LAST:event_btn_pesquisarMouseClicked

    private void txt_pesquisar_filmeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_pesquisar_filmeKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_pesquisar_filmeKeyTyped

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        String ordenar = null;

        switch (jComboBox1.getSelectedItem().toString()) {
            case "Data de Adição":
                ordenar = "data_inclusao";
                break;
            case "Data de Lançamento":
                System.out.println("oii");
                ordenar = "data_lancamento";
                break;
            case "Nome":
                ordenar = "nome";
                break;
            default:
                throw new AssertionError();
        }
        String sql = "SELECT id_midia, nome, img from midia ORDER BY " + ordenar;

        try{
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            popular(rs);
        }catch (Exception e){
            JOptionPane.showMessageDialog(rootPane, e);
        }

    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void cad_usrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cad_usrActionPerformed
        // TODO add your handling code here:
        TelaCadastro tc = new TelaCadastro();
        tc.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_cad_usrActionPerformed

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
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaInicial().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_pesquisar;
    private javax.swing.JButton btn_usr_alugueis;
    private javax.swing.JMenuItem cad_usr;
    private javax.swing.JLabel img_usuario;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txt_pesquisar_filme;
    private javax.swing.JLabel usr_nome;
    private javax.swing.JLabel usr_situacao;
    // End of variables declaration//GEN-END:variables
}
