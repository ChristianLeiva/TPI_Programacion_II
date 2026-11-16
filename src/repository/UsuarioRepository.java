/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import tpi_foodstore.Entities.Usuario;

/**
 *
 * @author Christian Leiva
 */
public class UsuarioRepository {
    
    private static List<Usuario> usuarios = new ArrayList<>();
    private static Long contadorI = 1L;
    
    //Alta de usuario en memoria con id autoincrement
    public Usuario usuario(Usuario usuario){
        usuario.setId(contadorI ++);
        usuario.setEliminado(false);
        usuarios.add(usuario);
        return usuario;
    }
    
    
   //Listar usuarios activos
    public List<Usuario> ListarActivos(){
        List<Usuario> activos = new ArrayList<>();
        for (Usuario user: usuarios){
            if(!user.isEliminado()){
                activos.add(user);
            }
            
        }
        return activos;
    }
    
    //Buscar por ID
    public Optional<Usuario> buscarPorId(Long id){
        for (Usuario user : usuarios){
            if(user.getId().equals(id)){
                return Optional.of(user);
            }            
        }
        return Optional.empty();
    }
    
    // Validar si el email existe (incluidos los usuarios eliminados)
    public boolean existeEmail(String mail){
        for (Usuario user : usuarios){
            if(user.getMail().equalsIgnoreCase(mail)){
                return true;
            }            
        }
        return false;
    }
}
