/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.slow.telas;
import java.sql.*;
import com.slow.dal.Conexao;
import static com.slow.telas.TelaInicial.id_midia;
import static com.slow.telas.TelaLogin.id_usr;
import static com.slow.telas.TelaLogin.situacao_usr;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.time.Instant;
import java.util.Date;
import java.time.temporal.ChronoUnit; 


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
    PreparedStatement pstAluga = null;
    PreparedStatement pstRevert = null;
    PreparedStatement pstLegenda = null;
    PreparedStatement pstAudio = null;
    ResultSet rs = null;
    static int qtd_copias = 0;
    
    public TelaInfoMidias() {
        initComponents();
        conexao = Conexao.conector();
        preencherTela();
        verificaPendencia();     
        tbl_audios.setDefaultEditor(Object.class, null);
        tbl_legendas.setDefaultEditor(Object.class, null);



    }

    private void verificaPendencia(){
        String sql = "select * from aluga where id_cli = ? and id_midia = ?";
        
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id_usr);
            pst.setString(2, id_midia);
            rs = pst.executeQuery();
            if (rs.next()){
                btn_alugar.setEnabled(false);
                dt_aluguel_midia.setEnabled(false);
                btn_cancelar_aluguel.setEnabled(true);
            } else if (!situacao_usr || qtd_copias <= 0){
                btn_alugar.setEnabled(false);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
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
        
        reverter = "DELETE from aluga where id_midia = ? and id_cli = ?";
        
        try{
            pstRevert = conexao.prepareStatement(reverter);
            pstRevert.setString(1, id_midia);
            pstRevert.setString(2, id_usr);

            
            int revertido = pstRevert.executeUpdate();

            // construir um negocio mais bonitinho se reverter nao der certo
        }catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, e);
        }
        
        qtd_copias +=1; 
        if(!dt_aluguel_midia.getDateFormatString().isEmpty()){
            btn_alugar.setEnabled(true);
            dt_aluguel_midia.setEnabled(true);
            btn_cancelar_aluguel.setEnabled(false);
        }
       
        
       
        
    }
    
    private void alugar(){
        String sql = "UPDATE midia set qtd_copias = qtd_copias-1 where id_midia = ?";
        try{
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id_midia);
            int adicionado = pst.executeUpdate();

            if (adicionado > 0){
                String insert_aluga = "INSERT into aluga(id_cli, id_midia, data_inicio, data_limite) values (?,?,?,?)";
                pstAluga = conexao.prepareStatement(insert_aluga);
                pstAluga.setString(1, id_usr);
                pstAluga.setString(2, id_midia);
                pstAluga.setDate(3, new java.sql.Date(Date.from(Instant.now()).getTime()));
                pstAluga.setDate(4, new java.sql.Date(dt_aluguel_midia.getDate().getTime()));
                

                int adicionadoAluga = pstAluga.executeUpdate();

                // vai ser uma funcao meio porca;
                if (adicionadoAluga > 0){
                    qtd_copias -=1; 
                    txt_copias_midia.setText("Quantidade de cópias: " + qtd_copias);

                }else{
                    cancelarAluguel();
                }
                
                btn_alugar.setEnabled(false);
                dt_aluguel_midia.setEnabled(false);
                dt_aluguel_midia.setDate(null);
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
                Image img = image.getScaledInstance(250,370,Image.SCALE_SMOOTH);
                img_midia_info.setIcon(new ImageIcon(img));
                boolean adulto = rs.getBoolean(4); 
                
                if (!adulto){
                    icon_adulto.setVisible(false);
                }
                java.util.Date format_br = new SimpleDateFormat("yyyy-dd-MM").parse(rs.getDate(5).toString());
                txt_data_midia.setText(new SimpleDateFormat("yyyy").format(format_br));
                qtd_copias = rs.getInt(6);
                txt_copias_midia.setText("Quantidade de cópias: " + qtd_copias);

                String legendaPesquisa = "select lm.nome_legenda as Legendas from midia m inner join legenda_midia lm on lm.id_midia = m.id_midia where m.id_midia = ?";
                try {
                    pstLegenda = conexao.prepareStatement(legendaPesquisa);
                    pstLegenda.setString(1, id_midia);
                    rs = pstLegenda.executeQuery();
                    tbl_legendas.setModel(DbUtils.resultSetToTableModel(rs));
                    
                } catch (Exception e) {
                    pnl_legenda.setEnabled(false);
                }
                
                String audioPesquisa = "select am.nome_audio as Dublagens from midia m inner join audio_midia am on am.id_midia = m.id_midia where m.id_midia = ?";
                try {
                    pstAudio = conexao.prepareStatement(audioPesquisa);
                    pstAudio.setString(1, id_midia);
                    rs = pstAudio.executeQuery();
                    tbl_audios.setModel(DbUtils.resultSetToTableModel(rs));

                } catch (Exception e) {
                    pnl_legenda.setEnabled(false);
                }
                
                                
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

        jPanel1 = new javax.swing.JPanel();
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
        pnl_legenda = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_legendas = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_audios = new javax.swing.JTable();
        dt_aluguel_midia = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(717, 460));

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
        btn_alugar.setEnabled(false);
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

        tbl_legendas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tbl_legendas);

        pnl_legenda.addTab("Legendas", jScrollPane2);

        tbl_audios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tbl_audios);

        pnl_legenda.addTab("Audio", jScrollPane3);

        dt_aluguel_midia.setDateFormatString("dd/mm/YYYY");
        dt_aluguel_midia.setMaxSelectableDate(Date.from(Instant.now().plus(30, ChronoUnit.DAYS)));
        dt_aluguel_midia.setMinSelectableDate(Date.from(Instant.now()));
        dt_aluguel_midia.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dt_aluguel_midiaPropertyChange(evt);
            }
        });

        jLabel1.setText("Alugar até");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_copias_midia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(img_midia_info, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(icon_adulto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_alugar)
                        .addGap(18, 18, 18)
                        .addComponent(btn_cancelar_aluguel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(185, 185, 185)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(dt_aluguel_midia, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)))
                .addGap(26, 26, 26))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(389, 389, 389)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(txt_nome_midia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txt_data_midia))
                        .addComponent(pnl_legenda, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(txt_categoria_midia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(txt_copias_midia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(img_midia_info, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(dt_aluguel_midia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(icon_adulto))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btn_alugar)
                                    .addComponent(btn_cancelar_aluguel))))
                        .addGap(2, 2, 2)))
                .addContainerGap(44, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_nome_midia)
                        .addComponent(txt_data_midia))
                    .addGap(12, 12, 12)
                    .addComponent(txt_categoria_midia)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(pnl_legenda, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(134, Short.MAX_VALUE)))
        );

        pnl_legenda.getAccessibleContext().setAccessibleName("Formularios");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(727, 490));
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

    private void dt_aluguel_midiaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dt_aluguel_midiaPropertyChange
        // TODO add your handling code here:
        Date selectedDate = dt_aluguel_midia.getDate();

        btn_alugar.setEnabled(selectedDate != null);
    }//GEN-LAST:event_dt_aluguel_midiaPropertyChange

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
    private com.toedter.calendar.JDateChooser dt_aluguel_midia;
    private javax.swing.JLabel icon_adulto;
    private javax.swing.JLabel img_midia_info;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane pnl_legenda;
    private javax.swing.JTable tbl_audios;
    private javax.swing.JTable tbl_legendas;
    private javax.swing.JLabel txt_categoria_midia;
    public static javax.swing.JLabel txt_copias_midia;
    private javax.swing.JLabel txt_data_midia;
    private javax.swing.JLabel txt_nome_midia;
    private javax.swing.JTextArea txt_sinopse_midia;
    // End of variables declaration//GEN-END:variables
}
