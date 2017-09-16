/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.windows;

import client.network.ClientNetwork;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;
import server.data.User;

/**
 *
 * @author elderjr
 */
public class RoomForm extends javax.swing.JDialog {

    private String roomName;
    private int maxPlayersPerTeam;
    private long matchTime;

    public RoomForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        this.roomName = null;
        this.maxPlayersPerTeam = -1;
        this.matchTime = -1;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        optionGroup = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        lbMatchTime = new javax.swing.JLabel();
        btCreateRoom = new javax.swing.JButton();
        lbRoomName = new javax.swing.JLabel();
        tfRoomName = new javax.swing.JTextField();
        btExit = new javax.swing.JButton();
        jcMatchTime = new javax.swing.JComboBox<>();
        lbPlayerPerTeam = new javax.swing.JLabel();
        jcPlayerPerTeam = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mainPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Room Form"));

        lbMatchTime.setText("Match Time:");

        btCreateRoom.setText("Create Room");
        btCreateRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCreateRoomActionPerformed(evt);
            }
        });

        lbRoomName.setText("Room Name");

        tfRoomName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfRoomNameActionPerformed(evt);
            }
        });

        btExit.setText("Exit");
        btExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExitActionPerformed(evt);
            }
        });

        jcMatchTime.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1m", "2m", "3m", "4m", "5m" }));

        lbPlayerPerTeam.setText("Player per Team:");

        jcPlayerPerTeam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 vs 1", "2 vs 2", "3 vs 3", "4 vs 4", "5 vs 5" }));

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(tfRoomName, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(lbRoomName)
                        .addGap(76, 76, 76)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(lbMatchTime)
                        .addGap(18, 18, 18)
                        .addComponent(lbPlayerPerTeam)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jcMatchTime, 0, 78, Short.MAX_VALUE)
                            .addComponent(btExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btCreateRoom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jcPlayerPerTeam, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbRoomName)
                    .addComponent(lbMatchTime)
                    .addComponent(lbPlayerPerTeam))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfRoomName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcMatchTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcPlayerPerTeam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btCreateRoom)
                    .addComponent(btExit)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public String getRoomName() {
        return this.roomName;
    }

    public int getMaxPlayersPerTeam() {
        return this.maxPlayersPerTeam;
    }

    public long getMatchTime() {
        return this.matchTime;
    }

    private void btCreateRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCreateRoomActionPerformed
        if (!tfRoomName.getText().isEmpty()) {
            this.roomName = tfRoomName.getText();
            this.maxPlayersPerTeam = this.jcPlayerPerTeam.getSelectedIndex() + 1;
            this.matchTime = (this.jcMatchTime.getSelectedIndex() + 1) * 60000;
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "The fields must be filled");
        }
    }//GEN-LAST:event_btCreateRoomActionPerformed

    private void btExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExitActionPerformed
        dispose();
    }//GEN-LAST:event_btExitActionPerformed

    private void tfRoomNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfRoomNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfRoomNameActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCreateRoom;
    private javax.swing.JButton btExit;
    private javax.swing.JComboBox<String> jcMatchTime;
    private javax.swing.JComboBox<String> jcPlayerPerTeam;
    private javax.swing.JLabel lbMatchTime;
    private javax.swing.JLabel lbPlayerPerTeam;
    private javax.swing.JLabel lbRoomName;
    private javax.swing.JPanel mainPanel;
    private javax.swing.ButtonGroup optionGroup;
    private javax.swing.JTextField tfRoomName;
    // End of variables declaration//GEN-END:variables
}
