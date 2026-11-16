🍔 Food Store | Trabajo Practico Integrador - Programacion II

Sistema de gestion y ventas para local gastronomico desarrollado en Java SE utilizando JDBC Puro y MySQL, implementando una arquitectura híbrida de almacenamiento y control transaccional robusto.

🚀 Arquitectura y Tecnologias

Lenguaje: Java 17+ Persistencia: JDBC Puro (Sin ORM / Sin Hibernate / Sin JPA) para control absoluto de sentencias SQL y transacciones. Base de Datos: MySQL 8 / 9. Modelo Híbrido: Persistencia en Base de Datos (JDBC): Categoria y Producto. Gestión Temporal en Memoria (Colecciones): Usuario, Pedido y DetallePedido.

🛠️ Funcionalidades Principales

ABM de Productos: Listado, Alta, Modificación y Baja Lógica conectados directamente a la base de datos con validaciones estrictas anti-negativos (precios y stocks). Pedido Interactivo: Selección dinámica de productos con control de stock en tiempo real y asignación de medio de pago al finalizar el carrito. Control Transaccional ACID (Rollback): Manejo de transacciones con setAutoCommit(false). Si ocurre un fallo parcial de stock durante el procesamiento del pedido, se evierte toda la operación automáticamente.

📂 Estructura del Proyecto
```text
TPI_FoodStore/
├── db/
│   └── script_creacion.sql       # Script SQL para la BD relacional (categoria y producto)
├── src/
│   ├── META-INF/
│   │   └── persistence.xml       # Configuración de conexión JDBC / Base de Datos
│   ├── repository/               # Repositorios en memoria para Usuario y Pedidos
│   └── tpi_foodstore/
│       ├── Entities/             # Clases de dominio (Base, Categoria, Producto, Usuario, Pedido, etc.)
│       ├── dao/                  # Objetos de Acceso a Datos (CategoriaDAO, ProductoDAO)
│       ├── service/              # Lógica de negocio y transacciones (PedidoService)
│       ├── enums/                # Enumeraciones (Rol, FormaPago, Estado)
│       ├── interfaces/           # Contratos genéricos (GenericDAO, Calculable)
│       └── Main.java             # Interfaz de consola interactiva (Menú principal y ABM)
├── Informe_Tecnico/
|   └── Informe_Tecnico_TPI.pdf   # Documentación formal del proyecto
└── README.md
```
⚙️ Configuración y Ejecución

Base de Datos: Ejecuta el script SQL ubicado en la carpeta db/ en tu servidor MySQL para crear la base de datos y sus tablas relacionales. Configura tus credenciales de acceso (usuario y contraseña) en la clase de conexión del paquete config.

Ejecución: Abre el proyecto en tu entorno de desarrollo favorito (NetBeans, IntelliJ IDEA o Eclipse). Ejecuta la clase principal Main.java para levantar el menú interactivo por consola.
