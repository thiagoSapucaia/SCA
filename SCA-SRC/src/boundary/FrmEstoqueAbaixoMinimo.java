package boundary;

import control.ControladoraMaterial;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import util.ActionFechar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class FrmEstoqueAbaixoMinimo extends javax.swing.JFrame {

    private ControladoraMaterial controladoraMaterial;
    private Vector materiais;

    public FrmEstoqueAbaixoMinimo(String servidor) {
        initComponents();
        this.setIconImage(new ImageIcon(getClass().getResource("/img/SCA-Logo_4.png")).getImage());
        this.controladoraMaterial = new ControladoraMaterial(servidor);
        preencherTabela();
        this.tbMateriais.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        adicionarMap();
        this.setLocationRelativeTo(null);
    }

    private void adicionarMap() {
        this.getRootPane().getActionMap().put("FECHAR", new ActionFechar(this));
        this.getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "FECHAR");
    }

    private void preencherTabela() {

        try {
            this.materiais = this.controladoraMaterial.obterMaterialAbaixoEstoque();
            DefaultTableModel modelo = (DefaultTableModel) this.tbMateriais.getModel();
            int numLinhas = this.materiais.size();
            for (int i = 0; i < numLinhas; i++) {
                modelo.insertRow(modelo.getRowCount(), (Vector) this.materiais.get(i));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao obter os Materiais \n\t Contate o suporte!", "Atenção", JOptionPane.OK_OPTION);
        }


    }

    private Vector criarLinhaTabela() {
        Vector linha = new Vector();
        for (int i = 0; i < 4; i++) {
            linha.add(this.tbMateriais.getModel().getValueAt(this.tbMateriais.getSelectedRow(), i));
        }
        return linha;
    }

    private void removerLinhaTabela() {
        DefaultTableModel modelo = (DefaultTableModel) this.tbMateriais.getModel();
        modelo.removeRow(this.tbMateriais.getSelectedRow());
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbMateriais = new javax.swing.JTable();
        btCiente = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("SCA-Estoque Abaixo do Minimo");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Lista de materias que possuem estoque abaixo do mínimo:");

        jButton1.setText("Fechar");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        tbMateriais.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Material", "Estoque Mínimo", "Estoque Atual"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbMateriais.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbMateriais);
        tbMateriais.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tbMateriais.getColumnModel().getColumn(0).setMinWidth(50);
        tbMateriais.getColumnModel().getColumn(0).setMaxWidth(50);
        tbMateriais.getColumnModel().getColumn(2).setMinWidth(90);
        tbMateriais.getColumnModel().getColumn(2).setMaxWidth(90);
        tbMateriais.getColumnModel().getColumn(3).setMinWidth(85);
        tbMateriais.getColumnModel().getColumn(3).setMaxWidth(85);

        btCiente.setText("Ciente");
        btCiente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btCienteMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btCiente, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btCiente)
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btCienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btCienteMouseClicked
        if (this.tbMateriais.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Você deve Selecionar um Material!", "Atenção", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                this.controladoraMaterial.darCienciaMaterial(criarLinhaTabela());
                removerLinhaTabela();
            } catch (SQLException ex) {
                Logger.getLogger(FrmEstoqueAbaixoMinimo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btCienteMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    }//GEN-LAST:event_formWindowClosing

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        this.dispose();
    }//GEN-LAST:event_jButton1MouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCiente;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbMateriais;
    // End of variables declaration//GEN-END:variables

    public Vector getMateriais() {
        return materiais;
    }
}
