/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.slow.telas;
import java.sql.*;
import com.slow.dal.Conexao;
import static com.slow.telas.TelaInicial.id_midia;
import static com.slow.telas.TelaLogin.id_usuario;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
/**
 *
 * @author sant
 */
public class TelaInfoMidias extends javax.swing.JFrame {

    /**
     * Creates new form TelaInfoMidias
     */
    Connection conexao = null;
    PreparedStatement pst = null;
    PreparedStatement pst2 = null;
    PreparedStatement pstRevert = null;
    ResultSet rs = null;
    static int qtd_copias = 0;
    
    public TelaInfoMidias() {
        initComponents();
        conexao = Conexao.conector();
        preencherTela();
        
        if(qtd_copias <= 0){
            btn_alugar.setEnabled(false);
        }

    }
    private void cancelarAluguel(){
        String reverter = "UPDATE midia set qtd_copias = qtd_copias+1 where id_midia = ?";
        try{
            pstRevert = conexao.prepareStatement(reverter);
            pstRevert.setString(1, id_midia);

            int revertido = pstRevert.executeUpdate();

            if(revertido < 0){
                this.dispose();
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, e);
        }
        
        reverter = "REMOVE from midia_usuario where id_midia = ? and id_cli = ?";
        
        try{
            pstRevert = conexao.prepareStatement(reverter);
            pstRevert.setString(1, id_midia);
            pstRevert.setString(1, id_usuario);

            
            int revertido = pstRevert.executeUpdate();

            // construir um negocio mais bonitinho se reverter nao der certo
        }catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, e);
        }
        
    }
    
    private void alugar(){

        String sql = "UPDATE midia set qtd_copias = qtd_copias-1 where id_midia = ?";
        try{
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id_midia);
            int adicionado = pst.executeUpdate();

            if (adicionado > 0){
                String insert_aluga = "INSERT into aluga(id_cli, id_midia) values (?,?)";
                pst2 = conexao.prepareStatement(insert_aluga);
                pst2.setString(1, id_usuario);
                pst2.setString(2, id_midia);

                int adicionadoAluga = pst2.executeUpdate();

                // vai ser uma funcao meio porca;
                if (adicionadoAluga > 0){
                    qtd_copias -=1;
                    txt_copias_midia.setText("Quantidade de cópias: " + qtd_copias);

                }else{
                    cancelarAluguel();
                }
                
                btn_alugar.setEnabled(false);
                btn_cancelar_aluguel.setEnabled(true);

            }else{
                JOptionPane.showMessageDialog(null, "Não foi possível alugar o filme");
            }

        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private String pontuarTexto(String texto){
        if (texto.trim().isEmpty()) {
            return texto;
        }
        
        String[] partes = texto.split(" ");
        
        
        if (partes.length > 1) {
            texto = texto.replaceAll(" +", ", ");
            return texto.substring(0, texto.length() - 2);
        }
        
        
        
        return texto;
    }
    
    private void preencherTela(){
        String sql = "SELECT nome, sinopse, img, adulto, data_lancamento, qtd_copias from midia where id_midia = ?";
        
        try{
            pst = conexao.prepareStatement(sql);
            pst.setString(1, TelaInicial.id_midia);

            rs = pst.executeQuery();
            if (rs.next()){
                txt_nome_midia.setText(rs.getString(1));
                txt_sinopse_midia.setText(rs.getString(2));
                byte[] imageBytes = rs.getBytes(3);
                Image image=getToolkit().createImage(imageBytes);
                Image img = image.getScaledInstance(238,320,Image.SCALE_SMOOTH);
                img_midia_info.setIcon(new ImageIcon(img));
                boolean adulto = rs.getBoolean(4); 
                
                if (!adulto){
                    icon_adulto.setVisible(false);
                }
                java.util.Date format_br = new SimpleDateFormat("yyyy-dd-MM").parse(rs.getDate(5).toString());
                txt_data_midia.setText(new SimpleDateFormat("dd/MM/yyyy").format(format_br));
                
                qtd_copias = rs.getInt(6);
                txt_copias_midia.setText("Quantidade de cópias: " + qtd_copias);

                
                
                
                                
                String categoriasPesquisa = "select c.nome from midia m inner join midia_categoria mc on mc.id_midia = m.id_midia inner join categoria c on c.id_categoria = mc.id_categoria where m.id_midia = ?";
                try {
                    pst = conexao.prepareStatement(categoriasPesquisa);
                    pst.setString(1, TelaInicial.id_midia);

                    rs = pst.executeQuery();
                    
                    String texto_categorias = "";
                    while(rs.next()){

                        texto_categorias += rs.getString(1) + " ";
                    }

                    txt_categoria_midia.setText(pontuarTexto(texto_categorias));
               
                } catch (Exception e) {
                    System.out.println(e);
                    txt_categoria_midia.setText(null);
                }

            }
            
        }catch (Exception e){
            System.out.println(e);
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

        img_midia_info = new javax.swing.JLabel();
        txt_nome_midia = new javax.swing.JLabel();
        icon_adulto = new javax.swing.JLabel();
        txt_data_midia = new javax.swing.JLabel();
        txt_categoria_midia = new javax.swing.JLabel();
        txt_copias_midia = new javax.swing.JLabel();
        btn_alugar = new javax.swing.JButton();
        btn_cancelar_aluguel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_sinopse_midia = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        img_midia_info.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        img_midia_info.setMaximumSize(new java.awt.Dimension(238, 320));
        img_midia_info.setMinimumSize(new java.awt.Dimension(238, 320));
        img_midia_info.setPreferredSize(new java.awt.Dimension(238, 320));
        img_midia_info.setRequestFocusEnabled(false);

        txt_nome_midia.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        txt_nome_midia.setText("jLabel2");

        icon_adulto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/slow/icones/icon_18.png"))); // NOI18N

        txt_data_midia.setText("jLabel5");

        txt_categoria_midia.setFont(new java.awt.Font("Liberation Sans", 1, 13)); // NOI18N
        txt_categoria_midia.setForeground(new java.awt.Color(255, 51, 51));
        txt_categoria_midia.setText("jLabel6");

        txt_copias_midia.setText("jLabel7");

        btn_alugar.setText("Alugar");
        btn_alugar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_alugarActionPerformed(evt);
            }
        });

        btn_cancelar_aluguel.setText("Cancelar Aluguel");
        btn_cancelar_aluguel.setEnabled(false);
        btn_cancelar_aluguel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelar_aluguelActionPerformed(evt);
            }
        });

        txt_sinopse_midia.setColumns(20);
        txt_sinopse_midia.setLineWrap(true);
        txt_sinopse_midia.setRows(5);
        txt_sinopse_midia.setWrapStyleWord(true);
        txt_sinopse_midia.setFocusable(false);
        jScrollPane1.setViewportView(txt_sinopse_midia);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(img_midia_info, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(icon_adulto)
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_alugar)
                        .addGap(18, 18, 18)
                        .addComponent(btn_cancelar_aluguel)
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txt_nome_midia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(txt_copias_midia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txt_data_midia)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_categoria_midia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(txt_nome_midia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(txt_categoria_midia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_data_midia)
                            .addComponent(icon_adulto))
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_alugar)
                            .addComponent(btn_cancelar_aluguel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_copias_midia))
                    .addComponent(img_midia_info, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(717, 460));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_alugarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_alugarActionPerformed
        // TODO add your handling code here:
        
        alugar();
    }//GEN-LAST:event_btn_alugarActionPerformed

    private void btn_cancelar_aluguelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_aluguelActionPerformed
        // TODO add your handling code here:
        cancelarAluguel();
    }//GEN-LAST:event_btn_cancelar_aluguelActionPerformed

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
            java.util.logging.Logger.getLogger(TelaInfoMidias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaInfoMidias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaInfoMidias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaInfoMidias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaInfoMidias().setVisible(true);
            }
        });
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_alugar;
    private javax.swing.JButton btn_cancelar_aluguel;
    private javax.swing.JLabel icon_adulto;
    private javax.swing.JLabel img_midia_info;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel txt_categoria_midia;
    public static javax.swing.JLabel txt_copias_midia;
    private javax.swing.JLabel txt_data_midia;
    private javax.swing.JLabel txt_nome_midia;
    private javax.swing.JTextArea txt_sinopse_midia;
    // End of variables declaration//GEN-END:variables
}
