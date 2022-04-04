package controlador.controladorpaneles;

import modelo.usuario.Usuario;

import java.awt.event.ActionListener;

/**
 * Interface que abstrae la implementacion de controlador de paneles de objetos tipo T
 */
public interface PanelControlador<T> extends ActionListener {
    void setUsuarioLogueado(Usuario usuarioLogueado);
    void setPanel(T panel);
    void configurarPanel(Usuario usuarioLogueado);
}