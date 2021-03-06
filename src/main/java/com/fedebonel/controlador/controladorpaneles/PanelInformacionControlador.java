package com.fedebonel.controlador.controladorpaneles;

import com.fedebonel.modelo.usuario.Usuario;
import com.fedebonel.servicios.MunicipiosServicio;
import com.fedebonel.servicios.PresentacionesServicio;
import com.fedebonel.vista.StringsFinales;
import com.fedebonel.vista.errores.ErrorVistaGenerador;
import com.fedebonel.vista.menuprincipal.InformacionPanel;

import java.awt.event.ActionEvent;

/**
 * Controlador del panel informacion, funciona como un puente entre el usuario, la vista del panel de informacion
 * y el controlador de convocatorias
 */
public class PanelInformacionControlador implements PanelControlador<InformacionPanel> {
    /**
     * Servicio de Municipios
     */
    private final MunicipiosServicio municipiosServicio;
    /**
     * Servicio de Presentaciones
     */
    private final PresentacionesServicio presentacionesServicio;
    /**
     * Vista de menu principal gestionada por este controlador
     */
    private InformacionPanel informacionPanel;
    /**
     * Usuario que utilizara el panel
     */
    private Usuario usuarioLogueado;

    /**
     * Constructor del controlador del panel de informacion
     *
     * @param municipiosServicio     Servicio de municipios
     * @param presentacionesServicio Servicio de presentaciones
     */
    public PanelInformacionControlador(MunicipiosServicio municipiosServicio,
                                       PresentacionesServicio presentacionesServicio) {
        this.municipiosServicio = municipiosServicio;
        this.presentacionesServicio = presentacionesServicio;
    }

    /**
     * Asigna el usuario a utilizar el panel
     *
     * @param usuarioLogueado Usuario a utilizar el panel
     */
    @Override
    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    /**
     * Asigna la vista del panel de informacion que este controlador debe manejar
     *
     * @param vista Vista del panel de convocatorias a asignar
     */
    @Override
    public void setPanel(InformacionPanel vista) {
        this.informacionPanel = vista;
        vista.addControlador(this);
        configurarPanel(usuarioLogueado);
    }

    /**
     * Configura el panel de informacion en la vista del menu principal asignada para el usuario logueado
     */
    @Override
    public void configurarPanel(Usuario usuarioLogueado) {
        try {
            // Todos pueden ver esto
            informacionPanel.mostrarInformacion(municipiosServicio.leerTodo(), presentacionesServicio.leerTodo());
        } catch (Exception e) {
            ErrorVistaGenerador.mostrarErrorDB(e);
        }
    }

    /**
     * Lee las interacciones del usuario sobre las vistas que este controlador gestiona
     *
     * @param evento Evento generado por una interaccion del usuario
     */
    @Override
    public void actionPerformed(ActionEvent evento) {
        String accion = evento.getActionCommand();
        if (StringsFinales.ACTUALIZAR.equals(accion)) {
            try {
                informacionPanel
                        .mostrarInformacion(municipiosServicio.leerTodo(), presentacionesServicio.leerTodo());
            } catch (Exception e) {
                ErrorVistaGenerador.mostrarErrorDB(e);
            }
        }
    }
}
