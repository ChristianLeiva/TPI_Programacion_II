
package tpi_foodstore;

import static java.time.Clock.system;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import tpi_foodstore.Entities.Pedido;
import tpi_foodstore.Entities.Producto;
import tpi_foodstore.Entities.Usuario;
import tpi_foodstore.enumerations.FormaPago;
import tpi_foodstore.enumerations.Rol;
import tpi_foodstore.interfaces.ProductoDAO;
import tpi_foodstore.service.PedidoService;

/**
 *
 * @author Christian Leiva
 */
public class TPI_Foodstore {

public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductoDAO productoDAO = new ProductoDAO();
        PedidoService pedidoService = new PedidoService();

        System.out.println("==================================================");
        System.out.println("     BIENVENIDO AL SISTEMA TPI FOOD STORE         ");
        System.out.println("==================================================");

        int opcion = 0;
        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Listar Productos desde MySQL (JDBC)");
            System.out.println("2. Armar un Pedido Interactivo y Descontar Stock");
            System.out.println("3. Registrar Nuevo Producto (Alta)");
            System.out.println("4. Modificar Producto");
            System.out.println("5. Dar de Baja un Producto");
            System.out.println("6. Salir");
            System.out.print("Elige una opcion: ");
            
            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
            } else {
                scanner.next(); // limpiar entrada invalida
                opcion = 0;
                System.out.println("Por favor, ingresa un numero valido.");
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.println("\n--- LISTADO DE PRODUCTOS EN BASE DE DATOS ---");
                    try {
                        List<Producto> productos = productoDAO.listarTodos(); 
                        if (productos.isEmpty()) {
                            System.out.println("No hay productos cargados en la base de datos todavia.");
                        } else {
                            for (Producto p : productos) {
                                System.out.println("ID: " + p.getId() + " | " + p.getNombre() + " | Precio: $" + p.getPrecio() + " | Stock: " + p.getStock() + " | Disponible: " + (p.getDisponible() ? "Si" : "No"));
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Error al listar productos: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("\n--- NUEVO PEDIDO INTERACTIVO ---");
                    try {
                        Usuario cliente = new Usuario("Carlos", "Perez", "carlos@mail.com", "2984123456", "1234", Rol.USUARIO);
                        cliente.setId(1L);

                        Pedido nuevoPedido = new Pedido(System.currentTimeMillis(), FormaPago.EFECTIVO);

                        boolean agregando = true;
                        while (agregando) {
                            List<Producto> productos = productoDAO.listarTodos();
                            if (productos.isEmpty()) {
                                System.out.println("No hay productos disponibles para comprar.");
                                break;
                            }

                            System.out.println("\nProductos disponibles:");
                            for (Producto p : productos) {
                                if (p.getStock() > 0 && p.getDisponible()) {
                                    System.out.println(" [ID: " + p.getId() + "] " + p.getNombre() + " - Precio: $" + p.getPrecio() + " - Stock: " + p.getStock());
                                }
                            }

                            System.out.print("\nIngresa el ID del producto que deseas comprar (o 0 para cancelar): ");
                            long idProd = scanner.nextLong();

                            if (idProd == 0) {
                                break;
                            }

                            Optional<Producto> prodOpt = productoDAO.buscarPorId(idProd);
                            if (prodOpt.isEmpty()) {
                                System.out.println("Producto no encontrado o inactivo. Intenta de nuevo.");
                                continue;
                            }

                            Producto productoSeleccionado = prodOpt.get();

                            System.out.print("Ingresa la cantidad que deseas llevar de '" + productoSeleccionado.getNombre() + "': ");
                            int cantidad = scanner.nextInt();

                            if (cantidad <= 0) {
                                System.out.println("La cantidad debe ser mayor a cero.");
                                continue;
                            }

                            if (cantidad > productoSeleccionado.getStock()) {
                                System.out.println("Stock insuficiente. Solo hay " + productoSeleccionado.getStock() + " unidades disponibles.");
                                continue;
                            }

                            double subtotal = productoSeleccionado.getPrecio() * cantidad;
                            nuevoPedido.addDetallePedido(cantidad, subtotal, productoSeleccionado);
                            System.out.println("Item agregado al carrito! Subtotal parcial: $" + subtotal);

                            System.out.print("Deseas agregar otro producto? (1: Si, 2: No): ");
                            int seguir = scanner.nextInt();
                            if (seguir == 2) {
                                agregando = false;
                            }
                        }

                        if (!nuevoPedido.getDetalles().isEmpty()) {
                            System.out.println("\n----------------------------------------");
                            System.out.println("TOTAL DEL CARRITO: $" + nuevoPedido.getTotal());
                            System.out.println("----------------------------------------");
                            
                            System.out.println("Selecciona el medio de pago:");
                            System.out.println("1. EFECTIVO");
                            System.out.println("2. TARJETA");
                            System.out.println("3. TRANSFERENCIA");
                            System.out.print("Elige una opcion (1-3): ");
                            int opcionPago = scanner.nextInt();

                            FormaPago formaPagoSeleccionada = FormaPago.EFECTIVO;
                            switch (opcionPago) {
                                case 2:
                                    formaPagoSeleccionada = FormaPago.TARJETA;
                                    break;
                                case 3:
                                    formaPagoSeleccionada = FormaPago.TRANSFERENCIA;
                                    break;
                                default:
                                    formaPagoSeleccionada = FormaPago.EFECTIVO;
                                    break;
                            }
                            
                            nuevoPedido.setFormaPago(formaPagoSeleccionada);

                            System.out.println("\nProcesando pago y actualizando stock en MySQL...");
                            pedidoService.procesarPedido(nuevoPedido);
                            
                            System.out.println("\nCOMPRA FINALIZADA CON EXITO!");
                            System.out.println("Medio de pago: " + nuevoPedido.getFormaPago());
                            System.out.println("Monto total abonado: $" + nuevoPedido.getTotal());
                            System.out.println("----------------------------------------");
                        } else {
                            System.out.println("Operacion cancelada. No se agregaron productos al pedido.");
                        }

                    } catch (Exception e) {
                        System.err.println("Error al procesar el pedido interactivo: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("\n--- REGISTRAR NUEVO PRODUCTO ---");
                    try {
                        Producto nuevoP = new Producto();
                        scanner.nextLine(); // limpiar buffer
                        System.out.print("Nombre del producto: ");
                        nuevoP.setNombre(scanner.nextLine());

                        // Validación de Precio (no negativo)
                        double precio = -1;
                        while (precio < 0) {
                            System.out.print("Precio (debe ser mayor o igual a 0): ");
                            if (scanner.hasNextDouble()) {
                                precio = scanner.nextDouble();
                                if (precio < 0) System.out.println("El precio no puede ser negativo.");
                            } else {
                                System.out.println("Ingresa un numero valido.");
                                scanner.next();
                            }
                        }
                        nuevoP.setPrecio(precio);

                        scanner.nextLine(); // limpiar buffer
                        System.out.print("Descripcion: ");
                        nuevoP.setDescripcion(scanner.nextLine());

                        // Validación de Stock (no negativo)
                        int stock = -1;
                        while (stock < 0) {
                            System.out.print("Stock inicial (debe ser mayor o igual a 0): ");
                            if (scanner.hasNextInt()) {
                                stock = scanner.nextInt();
                                if (stock < 0) System.out.println("El stock no puede ser negativo.");
                            } else {
                                System.out.println("Ingresa un numero entero valido.");
                                scanner.next();
                            }
                        }
                        nuevoP.setStock(stock);

                        scanner.nextLine(); // limpiar buffer
                        System.out.print("Imagen (nombre o ruta): ");
                        nuevoP.setImagen(scanner.nextLine());

                        nuevoP.setDisponible(true);

                        System.out.print("ID de la categoria a la que pertenece: ");
                        Long idCat = scanner.nextLong();

                        Long idGenerado = productoDAO.insertar(nuevoP, idCat);
                        System.out.println("Producto registrado con exito! ID generado en MySQL: " + idGenerado);
                    } catch (Exception e) {
                        System.err.println("Error al registrar el producto: " + e.getMessage());
                    }
                    break;

                case 4:
                    System.out.println("\n--- MODIFICAR PRODUCTO ---");
                    try {
                        System.out.print("Ingresa el ID del producto que deseas modificar: ");
                        Long idMod = scanner.nextLong();

                        Optional<Producto> pOpt = productoDAO.buscarPorId(idMod);
                        if (pOpt.isEmpty()) {
                            System.out.println("Producto no encontrado o inactivo.");
                            break;
                        }

                        Producto p = pOpt.get();
                        scanner.nextLine(); // limpiar buffer
                        System.out.print("Nuevo nombre (actual: " + p.getNombre() + "): ");
                        String nom = scanner.nextLine();
                        if (!nom.isEmpty()) p.setNombre(nom);

                        System.out.print("Nuevo precio (actual: " + p.getPrecio() + ") [Enter para omitir]: ");
                        String precStr = scanner.nextLine();
                        if (!precStr.isEmpty()) {
                            double nuevoPrecio = Double.parseDouble(precStr);
                            while (nuevoPrecio < 0) {
                                System.out.print("El precio no puede ser negativo. Ingresa otro: ");
                                nuevoPrecio = scanner.nextDouble();
                            }
                            p.setPrecio(nuevoPrecio);
                        }

                        System.out.print("Nueva descripcion (actual: " + p.getDescripcion() + "): ");
                        String desc = scanner.nextLine();
                        if (!desc.isEmpty()) p.setDescripcion(desc);

                        System.out.print("Nuevo stock (actual: " + p.getStock() + ") [Enter para omitir]: ");
                        String stockStr = scanner.nextLine();
                        if (!stockStr.isEmpty()) {
                            int nuevoStock = Integer.parseInt(stockStr);
                            while (nuevoStock < 0) {
                                System.out.print("El stock no puede ser negativo. Ingresa otro: ");
                                nuevoStock = scanner.nextInt();
                            }
                            p.setStock(nuevoStock);
                        }

                        boolean modificado = productoDAO.modificar(p);
                        if (modificado) {
                            System.out.println("Producto modificado con exito en la base de datos!");
                        } else {
                            System.out.println("No se pudo modificar el producto.");
                        }
                    } catch (Exception e) {
                        System.err.println("Error al modificar el producto: " + e.getMessage());
                    }
                    break;

                case 5:
                    System.out.println("\n--- DAR DE BAJA UN PRODUCTO ---");
                    try {
                        System.out.print("Ingresa el ID del producto que deseas dar de baja: ");
                        Long idBaja = scanner.nextLong();

                        boolean dadoDeBaja = productoDAO.bajaLogica(idBaja);
                        if (dadoDeBaja) {
                            System.out.println("Producto dado de baja logicamente con exito!");
                        } else {
                            System.out.println("No se encontro el producto o ya se encontraba dado de baja.");
                        }
                    } catch (Exception e) {
                        System.err.println("Error al dar de baja el producto: " + e.getMessage());
                    }
                    break;

                case 6:
                    System.out.println("Gracias por usar Food Store! Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opcion invalida. Intenta nuevamente.");
            }

        } while (opcion != 6);

        scanner.close();
    }
}