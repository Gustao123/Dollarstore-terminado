
package vista;
import javax.swing.*;
import modelo.proveedor;
import modelo.DAOProveedor;
import modelo.DAOcompra;
import modelo.DAOproducto;
import modelo.compra;
import modelo.producto;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.DAOcompra;
import javax.swing.JOptionPane;

/**
 *
 * @author juriel
 */
public class JFramecompras extends javax.swing.JFrame {
 private DefaultTableModel modeloTablaCompra;
         private Object[] objetoCompraTabla = new Object[6];
         private Double total = 0.0;
         private int item = 0;
         private java.sql.Date fech;
         private int cantidad = 0;
         private int producto_id;
         private int  compraid = 0;
    /**
     * Creates new form JFramecompras
     */
    public JFramecompras() {
        initComponents();
         jTextdescripcion.setText("0");
        jDialogproveedores.setLocationRelativeTo(null);
        jDialogproducto.setLocationRelativeTo(null);
        jTextProveedor_id.setEnabled(false);
        jTextExistencia.setEnabled(false);
        jTextPrecioProduc.setEnabled(false);
        jTextproducto_id.setEnabled(false);
        jTextdescripcion.setEnabled(false);
        jTextProveedorNombre.setEnabled(false);
        //Obtiene la fecha actual
        fech = java.sql.Date.valueOf(LocalDate.now());
        jLabelfecha.setText(fech.toString());
        modeloTablaCompra=(DefaultTableModel) jTableproductoscompra.getModel();
    }
    
    
     public void limpiarCamposProductos(){
        jTextproducto_id.setText("0");
        jTextdescripcion.setText("");
        jTextCantidad.setText("0");
        jTextPrecioProduc.setText("0.0");
        jTextExistencia.setText("0");
        
    }
 
     private void buscaDatosProveedor(String dato) throws SQLException{
        //Se crea una lista que almacena los datos obtenidos de la BD
        List<proveedor> proveedores = new DAOProveedor().buscarproveedor(dato);
        DefaultTableModel modelo = new DefaultTableModel();
        //Arreglo con nombre de columnas de la tabla
        String[] columnas = {"Id", "Compañía", "Teléfono", "Correo_electrónico"};
         //Establece los nombres definidos de las columnas
        modelo.setColumnIdentifiers(columnas);
        for (proveedor prov : proveedores){
            //Recorre cada elemento de la lista y los agrega al modelo
            String[] renglon = {Integer.toString(prov.getproveedor_id()),
            prov.getCompany(), prov.getTelefono(), prov.getCorreo_electro()};
            modelo.addRow(renglon);
        } //Carga datos de la tabla
        jTDbuscaProveedor.setModel(modelo); //Ubica los datos del modelo en la tabla
    }
     
       private void buscaDatosProducto(String dato) throws SQLException {
            List<producto> productos = new DAOproducto().buscarproducto(dato);
            DefaultTableModel modelo = new DefaultTableModel();
            String[] columnas = {"Id", "Descripcion", "Cantidad", "Precio",
                "Existencia", "N°Marca", "N°Categoria"};
            modelo.setColumnIdentifiers(columnas);

            for (producto pro : productos) {
                if (pro == null) {
                    continue; // Omite cualquier producto que sea nulo
                }
                String[] renglon = {Integer.toString(pro.getProducto_id()), 
                    pro.getDescripcion(), Integer.toString(pro.getCantidad()),
                                    Double.toString(pro.getPrecio_produc()), 
                                    Integer.toString(pro.getExistencia()), 
                                    Integer.toString(pro.getMarca_id()),
                                    Integer.toString(pro.getCategoria_id())};
                modelo.addRow(renglon);
            }

            jTBuscaProductos.setModel(modelo);
        }
       
       public void guardarCompra() throws SQLException {
        if (jTextProveedorNombre.getText().contentEquals("")||
                jLabelfecha.getText().contentEquals("")|| 
                jTableproductoscompra.getRowCount() == 0){
            JOptionPane.showMessageDialog(rootPane, 
              "Se requieren datos del proveedor y producto, "
            + "fecha");
            
        } else {
            try{
                //Captura los datos de los jtext en JFrame Venta
                int id = Integer.parseInt(jTextProveedor_id.getText());
            
                compra comp = new compra(id, fech);
                //Objeto de DAOVenta
                DAOcompra daocompra = new DAOcompra();
                //Llamada al método insertar una venta, si el resultado es exitoso
                //regresa un 0
                if (daocompra.insertarCompra(comp) == 0){
                    JOptionPane.showMessageDialog(rootPane, 
                            "Registro agregado en Compra");
                    //------Obtiene el último número de factura generado------//
                       //Se le asigna a una venta global numfac
                    compraid = daocompra.obtenerUltimoNumCompra();

                } else {
                    JOptionPane.showMessageDialog(rootPane, 
                         "Ha ocurrido un error, no se insertó en Compra");
                }
            } catch (HeadlessException | SQLException e){
                 JOptionPane.showMessageDialog(rootPane, 
                         "No se agregaron los registros " + e);
            }
            JOptionPane.showMessageDialog(rootPane,
                    "Registro listo para agregar en Compra" + compraid);
            //*************Si la venta se guarda exitosa, se guardan los detalles*******
            guardarDetalleCompra();//Llamado al método que guarda los detalles
//*************Actualiza existencia de productos*******************
            actualizarExistencia();
        } 
    }
       
                 public void guardarDetalleCompra() throws SQLException {
        //Datos de jtable venta a guardar en detalle0
        //Número de factura (compraid), producto_id y cantidad están declarados de forma global
        double precio;
         //Se evalúa que jtable venta productos no esté vacía y que se haya  guardado la
        //venta factura
        if (compraid == 0 || jTableproductoscompra.getRowCount() == 0) {
            JOptionPane.showMessageDialog(rootPane, 
                    "No se ha obtenido el número de la factura o" + 
                    "No tiene productos añadidos para Comprar");
            
        } else {
            for (int i = 0; i < jTableproductoscompra.getRowCount(); i++) {
                 producto_id = Integer.parseInt(jTableproductoscompra.
                        getValueAt(i, 1).toString());
                cantidad = Integer.parseInt(jTableproductoscompra.
                        getValueAt(i, 3).toString());
                precio = Double.parseDouble(jTableproductoscompra.
                        getValueAt(i, 4).toString());
                
                //Objeto de Venta que invoca al constructor con los parámetros a
                //guardar detalle
                compra detallecomCompra = new compra(compraid, cantidad,
                        producto_id, precio);
                
                //Objeto de DAOVenta que invoca al método insertarDetalle
                DAOcompra daoDetalleCompra = new DAOcompra();
                if (daoDetalleCompra.insertarDetalleCompra(detallecomCompra) == 0){
                      JOptionPane.showMessageDialog(rootPane,
                         "Registro agregado en Detalle Compra");

                } else {
                      JOptionPane.showMessageDialog(rootPane, 
                  "Ha ocurrido un error, no se insertó el Detalle Compra");
                }
            }
        }
    }
                  public void actualizarExistencia() {
        int existenciaActual;
        int nuevaExistencia = 0;
        
        for (int i = 0; i < jTableproductoscompra.getRowCount(); i++){
             producto_id = Integer.parseInt(jTableproductoscompra.
                    getValueAt(i, 1).toString());
            cantidad = Integer.parseInt(jTableproductoscompra.
                    getValueAt(i, 3).toString());
            DAOproducto daopro = new DAOproducto();
            
            try {
                List<producto> p=daopro.buscarproducto(String.valueOf(producto_id).toString());
                   existenciaActual = p.get(0).getExistencia();
                nuevaExistencia =  existenciaActual + cantidad;
                producto pro = new producto(producto_id, nuevaExistencia);
                DAOcompra daocompra = new DAOcompra();
                if (daocompra.actualizarExistenciaProducto(pro) == 0) {
                    JOptionPane.showMessageDialog(rootPane,
                            "Existencia Actualizada");
                }
            } catch (SQLException ex) {
                Logger.getLogger(JFramecompras.class.getName()).
                        log(Level.SEVERE, null, ex);
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

        jDialogproveedores = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        jTextDBuscaProveedor = new javax.swing.JTextField();
        jBDbuscaPoveedor = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTDbuscaProveedor = new javax.swing.JTable();
        jBDañadirProveedor = new javax.swing.JButton();
        jDialogproducto = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jBDbuscaProducto = new javax.swing.JButton();
        jBDañadirProducto = new javax.swing.JButton();
        jTextDbuscaproducto = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTBuscaProductos = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableproductoscompra = new javax.swing.JTable();
        jLabeltotal = new javax.swing.JLabel();
        jBGuardarCompra = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jTextproducto_id = new javax.swing.JTextField();
        jTextdescripcion = new javax.swing.JTextField();
        jBbuscaProducto = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextPrecioProduc = new javax.swing.JTextField();
        jBañadirProducto = new javax.swing.JButton();
        jBQuitar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextExistencia = new javax.swing.JTextField();
        jTextCantidad = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTextProveedor_id = new javax.swing.JTextField();
        jTextProveedorNombre = new javax.swing.JTextField();
        jBbuscaProveedor = new javax.swing.JButton();
        jLabelfecha = new javax.swing.JLabel();

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Buscar Proveedores", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Serif", 2, 24), new java.awt.Color(51, 51, 255))); // NOI18N
        jPanel5.setToolTipText("");

        jTextDBuscaProveedor.setBackground(new java.awt.Color(255, 255, 255));

        jBDbuscaPoveedor.setBackground(new java.awt.Color(255, 255, 255));
        jBDbuscaPoveedor.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jBDbuscaPoveedor.setForeground(new java.awt.Color(0, 0, 0));
        jBDbuscaPoveedor.setText("Buscar");
        jBDbuscaPoveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBDbuscaPoveedorActionPerformed(evt);
            }
        });

        jTDbuscaProveedor.setBackground(new java.awt.Color(153, 153, 255));
        jTDbuscaProveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTDbuscaProveedor);

        jBDañadirProveedor.setBackground(new java.awt.Color(255, 255, 255));
        jBDañadirProveedor.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jBDañadirProveedor.setForeground(new java.awt.Color(0, 0, 0));
        jBDañadirProveedor.setText("Añadir");
        jBDañadirProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBDañadirProveedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jTextDBuscaProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBDbuscaPoveedor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBDañadirProveedor)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextDBuscaProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBDbuscaPoveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBDañadirProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialogproveedoresLayout = new javax.swing.GroupLayout(jDialogproveedores.getContentPane());
        jDialogproveedores.getContentPane().setLayout(jDialogproveedoresLayout);
        jDialogproveedoresLayout.setHorizontalGroup(
            jDialogproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogproveedoresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialogproveedoresLayout.setVerticalGroup(
            jDialogproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogproveedoresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(153, 153, 153));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "BuscarProductos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Serif", 3, 24), new java.awt.Color(51, 51, 255))); // NOI18N

        jBDbuscaProducto.setBackground(new java.awt.Color(255, 255, 255));
        jBDbuscaProducto.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jBDbuscaProducto.setForeground(new java.awt.Color(0, 0, 0));
        jBDbuscaProducto.setText("Buscar");
        jBDbuscaProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBDbuscaProductoActionPerformed(evt);
            }
        });

        jBDañadirProducto.setBackground(new java.awt.Color(255, 255, 255));
        jBDañadirProducto.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jBDañadirProducto.setForeground(new java.awt.Color(0, 0, 0));
        jBDañadirProducto.setText("Añadir");
        jBDañadirProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBDañadirProductoActionPerformed(evt);
            }
        });

        jTextDbuscaproducto.setBackground(new java.awt.Color(255, 255, 255));

        jTBuscaProductos.setBackground(new java.awt.Color(153, 153, 255));
        jTBuscaProductos.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jTBuscaProductos.setForeground(new java.awt.Color(0, 0, 0));
        jTBuscaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTBuscaProductos);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jTextDbuscaproducto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBDbuscaProducto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBDañadirProducto)))
                .addGap(109, 109, 109))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextDbuscaproducto)
                    .addComponent(jBDbuscaProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(jBDañadirProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialogproductoLayout = new javax.swing.GroupLayout(jDialogproducto.getContentPane());
        jDialogproducto.getContentPane().setLayout(jDialogproductoLayout);
        jDialogproductoLayout.setHorizontalGroup(
            jDialogproductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogproductoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialogproductoLayout.setVerticalGroup(
            jDialogproductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogproductoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registros", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Serif", 3, 24), new java.awt.Color(51, 51, 255))); // NOI18N

        jTableproductoscompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IDProveedor", "IDProducto", "Descripción", "Cantidad", "Precio", "Subtotal"
            }
        ));
        jScrollPane1.setViewportView(jTableproductoscompra);

        jLabeltotal.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabeltotal.setForeground(new java.awt.Color(0, 0, 0));
        jLabeltotal.setText("Total:");

        jBGuardarCompra.setBackground(new java.awt.Color(50, 195, 45));
        jBGuardarCompra.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jBGuardarCompra.setForeground(new java.awt.Color(0, 0, 0));
        jBGuardarCompra.setText("Guardar");
        jBGuardarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBGuardarCompraActionPerformed(evt);
            }
        });

        jBCancelar.setBackground(new java.awt.Color(255, 51, 51));
        jBCancelar.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jBCancelar.setForeground(new java.awt.Color(0, 0, 0));
        jBCancelar.setText("Cancelar");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jBGuardarCompra)
                        .addGap(38, 38, 38)
                        .addComponent(jBCancelar)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabeltotal)
                        .addGap(150, 150, 150))))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabeltotal)
                .addGap(31, 31, 31)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBGuardarCompra)
                    .addComponent(jBCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del Producto", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Serif", 3, 24), new java.awt.Color(51, 51, 255))); // NOI18N

        jTextproducto_id.setBackground(new java.awt.Color(255, 255, 255));
        jTextproducto_id.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jTextproducto_id.setForeground(new java.awt.Color(0, 0, 0));
        jTextproducto_id.setText("Id");
        jTextproducto_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextproducto_idActionPerformed(evt);
            }
        });

        jTextdescripcion.setBackground(new java.awt.Color(255, 255, 255));
        jTextdescripcion.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jTextdescripcion.setForeground(new java.awt.Color(0, 0, 0));
        jTextdescripcion.setText("Nombre");
        jTextdescripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextdescripcionKeyTyped(evt);
            }
        });

        jBbuscaProducto.setBackground(new java.awt.Color(255, 255, 255));
        jBbuscaProducto.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jBbuscaProducto.setForeground(new java.awt.Color(0, 0, 0));
        jBbuscaProducto.setText("Buscar");
        jBbuscaProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBbuscaProductoActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Precio:");

        jTextPrecioProduc.setEditable(false);
        jTextPrecioProduc.setBackground(new java.awt.Color(255, 255, 255));
        jTextPrecioProduc.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jTextPrecioProduc.setForeground(new java.awt.Color(0, 0, 0));

        jBañadirProducto.setBackground(new java.awt.Color(255, 255, 255));
        jBañadirProducto.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jBañadirProducto.setForeground(new java.awt.Color(0, 0, 0));
        jBañadirProducto.setText("Añadir");
        jBañadirProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBañadirProductoActionPerformed(evt);
            }
        });

        jBQuitar.setBackground(new java.awt.Color(255, 255, 255));
        jBQuitar.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jBQuitar.setForeground(new java.awt.Color(0, 0, 0));
        jBQuitar.setText("Quitar");
        jBQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBQuitarActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Cantidad:");

        jLabel6.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Existencia:");

        jTextExistencia.setBackground(new java.awt.Color(255, 255, 255));
        jTextExistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextExistenciaActionPerformed(evt);
            }
        });

        jTextCantidad.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jTextproducto_id, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextdescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBbuscaProducto))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel2))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(jTextPrecioProduc, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextExistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(69, 69, 69)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jBañadirProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBQuitar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextproducto_id, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextdescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBbuscaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2)
                        .addComponent(jTextPrecioProduc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jBañadirProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextExistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBQuitar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(102, 102, 255));

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setFont(new java.awt.Font("Serif", 3, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Compras");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Buscar Proveedor", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Serif", 3, 24), new java.awt.Color(51, 51, 255))); // NOI18N

        jTextProveedor_id.setBackground(new java.awt.Color(255, 255, 255));
        jTextProveedor_id.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jTextProveedor_id.setForeground(new java.awt.Color(0, 0, 0));
        jTextProveedor_id.setText("Id:");

        jTextProveedorNombre.setBackground(new java.awt.Color(255, 255, 255));
        jTextProveedorNombre.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jTextProveedorNombre.setForeground(new java.awt.Color(0, 0, 0));
        jTextProveedorNombre.setText("Nombre:");
        jTextProveedorNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextProveedorNombreKeyTyped(evt);
            }
        });

        jBbuscaProveedor.setBackground(new java.awt.Color(255, 255, 255));
        jBbuscaProveedor.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jBbuscaProveedor.setForeground(new java.awt.Color(0, 0, 0));
        jBbuscaProveedor.setText("Buscar");
        jBbuscaProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBbuscaProveedorActionPerformed(evt);
            }
        });

        jLabelfecha.setFont(new java.awt.Font("Serif", 3, 14)); // NOI18N
        jLabelfecha.setForeground(new java.awt.Color(0, 0, 0));
        jLabelfecha.setText("Fecha");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextProveedor_id, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextProveedorNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBbuscaProveedor)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelfecha)
                .addGap(76, 76, 76))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelfecha)
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jBbuscaProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextProveedorNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextProveedor_id, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(82, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBGuardarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBGuardarCompraActionPerformed
        // TODO add your handling code here:
        try{
            guardarCompra();//Llama al método que guarda en Venta
        } catch (SQLException ex) {
            Logger.getLogger(JFramecompras.class.getName()).
            log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBGuardarCompraActionPerformed

    private void jTextproducto_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextproducto_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextproducto_idActionPerformed

    private void jTextdescripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextdescripcionKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if ((car < 'a' || car > 'z')&& (car < 'A' || car > 'Z' )
            && car != 'á' //Minúsculas
            && car != 'é'
            && car != 'í'
            && car != 'ó'
            && car != 'ú'
            && car != 'Á'
            && car != 'É'
            && car != 'Í'
            && car != 'Ú'
            && car != 'Ü'
            && car != 'ü'
            && car != 'Ñ'
            && car != 'ñ'
            && (car != (char) KeyEvent.VK_SPACE)){
            evt.consume();
        }
    }//GEN-LAST:event_jTextdescripcionKeyTyped

    private void jBbuscaProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBbuscaProductoActionPerformed
        // TODO add your handling code here:
        jDialogproducto.setVisible(true);
    }//GEN-LAST:event_jBbuscaProductoActionPerformed

    private void jBañadirProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBañadirProductoActionPerformed
        // TODO add your handling code here:
        if (jTextCantidad.getText().contentEquals("0")){
            JOptionPane.showMessageDialog(rootPane,
                "Escriba la cantidad de producto");
        
            } else {
                //Item declarado de forma global
                item += 1;//Para numerar los productos agregados al jtableVenta
                //producto: cada dato se muestra luego en el jtable venta producto
                objetoCompraTabla[0] = item;
                //.trim() es para suprimir espacios en caso que existan
                objetoCompraTabla[1] = jTextproducto_id.getText().trim();
                objetoCompraTabla[2] = jTextdescripcion.getText().trim();
                objetoCompraTabla[3] = jTextCantidad.getText().trim();
                objetoCompraTabla[4] = jTextPrecioProduc.getText().trim();
                //Calcula el subtotal de cada fila en la columna jtable venta producto
                Double subtotal = Double.parseDouble(jTextCantidad.getText().trim())
                * Double.parseDouble(jTextPrecioProduc.getText().trim());
                //Muestra subtotal calculado en la columna de jtable venta producto
                objetoCompraTabla[5] = subtotal;
                //Suma cada subtotal al total general
                total += subtotal; //Total es declarado de forma legal
                //Muestra el total de los productos del jtable venta producto
                jLabeltotal.setText(total.toString());

                //Se agregan los datos del objeto al modelo de JTable venta producto
                modeloTablaCompra.addRow(objetoCompraTabla);
                limpiarCamposProductos();
            }
        
    }//GEN-LAST:event_jBañadirProductoActionPerformed

    private void jTextProveedorNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextProveedorNombreKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if ((car < 'a' || car > 'z')&& (car < 'A' || car > 'Z' )
            && car != 'á' //Minúsculas
            && car != 'é'
            && car != 'í'
            && car != 'ó'
            && car != 'ú'
            && car != 'Á'
            && car != 'É'
            && car != 'Í'
            && car != 'Ú'
            && car != 'Ü'
            && car != 'ü'
            && car != 'Ñ'
            && car != 'ñ'
            && (car != (char) KeyEvent.VK_SPACE)){
            evt.consume();
        }
    }//GEN-LAST:event_jTextProveedorNombreKeyTyped

    private void jBbuscaProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBbuscaProveedorActionPerformed
        // TODO add your handling code here:
        jDialogproveedores.setVisible(true);
    }//GEN-LAST:event_jBbuscaProveedorActionPerformed

    private void jBDbuscaPoveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBDbuscaPoveedorActionPerformed
        // TODO add your handling code here:
        if (jTextDBuscaProveedor.getText().contentEquals("")){
            JOptionPane.showMessageDialog(rootPane,
                "Ingrese texto a buscar");
        } else {
            try{
                //Se obtiene el dato de la caja de texto, para realizar búsqueda
                String dato = jTextDBuscaProveedor.getText();

                //Llamada al procedimiento almacenado
                buscaDatosProveedor(dato);
                jTextDBuscaProveedor.setText("");

            } catch (SQLException ex) {
                Logger.getLogger(JFramecompras.class.getName()).
                log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jBDbuscaPoveedorActionPerformed

    private void jBDañadirProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBDañadirProveedorActionPerformed
        // TODO add your handling code here:
        int fila = this.jTDbuscaProveedor.getSelectedRow();//Se obtiene el #fila seleccionada
        if (fila == -1){
            JOptionPane.showMessageDialog(rootPane,
                "Seleccione un registro de la tabla");
        } else { //Se toma cada campo de la tabla del registro seleccionado
            // y se asigna a una variable
            try{
                int id = Integer.parseInt((String)
                    this.jTDbuscaProveedor.getValueAt(fila, 0));
                String comp = (String)
                this.jTDbuscaProveedor.getValueAt(fila, 1);
                String tel = (String)
                this.jTDbuscaProveedor.getValueAt(fila, 2);
                //Se ubican en las cajas de textos los datos capturados de la tabla
                //Datos ubicados en datos del cliente del Frame Venta
                jTextProveedor_id.setText(""+id);
                jTextProveedorNombre.setText(comp +"" +tel);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(rootPane,
                    "¡Ocurrió un error!");
            }
        }

    }//GEN-LAST:event_jBDañadirProveedorActionPerformed

    private void jBDbuscaProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBDbuscaProductoActionPerformed
        // TODO add your handling code here:
        if (jTextDbuscaproducto.getText().contentEquals("")){
            JOptionPane.showMessageDialog(rootPane,
                "Ingrese texto a buscar");
        } else {
            try{
                //Se obtiene el dato de la caja de texto, para realizar búsqueda
                String dato = jTextDbuscaproducto.getText();

                //Llamada al procedimiento almacenado
                buscaDatosProducto(dato);
                jTextDbuscaproducto.setText("");

            } catch (SQLException ex) {
                Logger.getLogger(JFramecompras.class.getName()).
                log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jBDbuscaProductoActionPerformed

    private void jBDañadirProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBDañadirProductoActionPerformed
        // TODO add your handling code here:
        int fila = this.jTBuscaProductos.getSelectedRow(); // Se obtiene #fila seleccionada
        if (fila == -1) {
            JOptionPane.showMessageDialog(rootPane,
                "Seleccione un registro de la tabla");
        } else {
            try {
                // Obtener los valores directamente como se almacenan en la tabla
                int id = Integer.parseInt(this.jTBuscaProductos.getValueAt(fila, 0).toString());
                String desc = this.jTBuscaProductos.getValueAt(fila, 1).toString();
                int cantidad = Integer.parseInt(this.jTBuscaProductos.getValueAt(fila, 2).toString());
                double prec = Double.parseDouble(this.jTBuscaProductos.getValueAt(fila, 3).toString());
                int exist = Integer.parseInt(this.jTBuscaProductos.getValueAt(fila, 4).toString());
                int marcaId = Integer.parseInt(this.jTBuscaProductos.getValueAt(fila, 5).toString());
                int categoriaId = Integer.parseInt(this.jTBuscaProductos.getValueAt(fila, 6).toString());

                // Se ubican en las cajas de textos los datos capturados de la tabla productos
                jTextproducto_id.setText(String.valueOf(id));
                jTextdescripcion.setText(desc);
                jTextCantidad.setText(String.valueOf(cantidad));
                jTextPrecioProduc.setText(String.valueOf(prec));
                jTextExistencia.setText(String.valueOf(exist));

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(rootPane,
                    "¡Ocurrió un ERROR! " + e.getMessage());
            }
        }
    }//GEN-LAST:event_jBDañadirProductoActionPerformed

    private void jTextExistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextExistenciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextExistenciaActionPerformed

    private void jBQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBQuitarActionPerformed
        // TODO add your handling code here:
         int fila = this.jTableproductoscompra.getSelectedRow();
        if (fila == -1 ){
            JOptionPane.showMessageDialog(rootPane, 
                    "Seleccione una fila de la tabla");
            
        } else {
            JDialog.setDefaultLookAndFeelDecorated(true);
            int resp = JOptionPane.showConfirmDialog(null, 
                    "¿Está seguro de quitar el producto de la lista?", 
                    "Aceptar", JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE);
            if (resp == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(rootPane, 
                        "No se ha retirado ningún producto");
                
            } else {
                if (resp == JOptionPane.YES_OPTION) {
                    DefaultTableModel temp = (DefaultTableModel)
                            jTableproductoscompra.getModel();
                 temp.removeRow(fila);
                 for (int i = 0; i < jTableproductoscompra.getRowCount(); i++) {
                      total = 0.0;
                      total += Double.parseDouble(jTableproductoscompra.
                              getValueAt(i, 5).toString());
                 }
                 jLabeltotal.setText(total.toString());
             }
                if (resp == JOptionPane.CLOSED_OPTION) {
                    JOptionPane.showMessageDialog(rootPane, 
                            "Ninguna acción realizada");
                }
          }
      }

    }//GEN-LAST:event_jBQuitarActionPerformed

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
            java.util.logging.Logger.getLogger(JFramecompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFramecompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFramecompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFramecompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFramecompras().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBDañadirProducto;
    private javax.swing.JButton jBDañadirProveedor;
    private javax.swing.JButton jBDbuscaPoveedor;
    private javax.swing.JButton jBDbuscaProducto;
    private javax.swing.JButton jBGuardarCompra;
    private javax.swing.JButton jBQuitar;
    private javax.swing.JButton jBañadirProducto;
    private javax.swing.JButton jBbuscaProducto;
    private javax.swing.JButton jBbuscaProveedor;
    private javax.swing.JDialog jDialogproducto;
    private javax.swing.JDialog jDialogproveedores;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelfecha;
    private javax.swing.JLabel jLabeltotal;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTBuscaProductos;
    private javax.swing.JTable jTDbuscaProveedor;
    private javax.swing.JTable jTableproductoscompra;
    private javax.swing.JTextField jTextCantidad;
    private javax.swing.JTextField jTextDBuscaProveedor;
    private javax.swing.JTextField jTextDbuscaproducto;
    private javax.swing.JTextField jTextExistencia;
    private javax.swing.JTextField jTextPrecioProduc;
    private javax.swing.JTextField jTextProveedorNombre;
    private javax.swing.JTextField jTextProveedor_id;
    private javax.swing.JTextField jTextdescripcion;
    private javax.swing.JTextField jTextproducto_id;
    // End of variables declaration//GEN-END:variables
}
