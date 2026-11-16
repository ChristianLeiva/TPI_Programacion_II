# Creación de la base de datos
CREATE DATABASE IF NOT EXISTS fooodstore;

USE fooodstore;

# Creación de la tabla Categoria
CREATE TABLE IF NOT EXISTS categoria (
	id INT AUTO_INCREMENT PRIMARY KEY,    
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    eliminado BOOLEAN NOT NULL,
    created_at DATETIME    
);

# Creación de la tabla Producto
CREATE TABLE IF NOT EXISTS producto (
	id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DOUBLE NOT NULL,
    stock INT NOT NULL,
    descripcion VARCHAR(255),
    imagen VARCHAR(255),
    disponible BOOLEAN NOT NULL,
    eliminado BOOLEAN NOT NULL,
    created_at DATETIME,
    categoria_id INT NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

/**/

-- ==========================================================
-- CARGA DE 10 CATEGORÍAS
-- ==========================================================
INSERT INTO categoria (nombre, descripcion, eliminado, created_at) VALUES 
('Smash Burgers', 'Nuestras infaltables aplastadas en plancha hirviendo con doble cheddar', FALSE, CURRENT_TIMESTAMP), -- ID 1
('Papas & Acompañamientos', 'Bañadas en salsas de la casa y crocantes al 100%', FALSE, CURRENT_TIMESTAMP), -- ID 2
('Pizzas de Masa Madre', 'Fermentación en frío de 48 horas, horno de barro', FALSE, CURRENT_TIMESTAMP), -- ID 3
('Empanadas Criollas', 'Cortadas a cuchillo y horneadas a la leña', FALSE, CURRENT_TIMESTAMP), -- ID 4
('Sándwiches & Lomos', 'Pan casero tostado con manteca y carnes seleccionadas', FALSE, CURRENT_TIMESTAMP), -- ID 5
('Ensaladas Frescas', 'Opciones bien cargadas para los que quieren cuidar la línea sin pasar hambre', FALSE, CURRENT_TIMESTAMP), -- ID 6
('Bebidas & Cervezas', 'Artesanales tiradas y gaseosas bien frías', FALSE, CURRENT_TIMESTAMP), -- ID 7
('Postres Caseros', 'El toque dulce para cerrar la noche como se debe', FALSE, CURRENT_TIMESTAMP), -- ID 8
('Pizzas Vegetarianas', 'Cargadas de vegetales grillados y mucho sabor', FALSE, CURRENT_TIMESTAMP), -- ID 9
('Promos del Chef', 'Combos infalibles para compartir o devorarte solo', FALSE, CURRENT_TIMESTAMP); -- ID 10


-- ==========================================================
-- CARGA DE 20 PRODUCTOS (Vinculados a sus respectivas categorías)
-- ==========================================================
INSERT INTO producto (nombre, precio, descripcion, stock, imagen, disponible, eliminado, created_at, categoria_id) VALUES 
-- Smash Burgers (Cat 1)
('La Onion Smash', 7800.0, 'Doble medallón de carne, mucho cheddar fundido, cebolla crispy y salsa secreta de la casa', 25, 'onion_smash.png', TRUE, FALSE, CURRENT_TIMESTAMP, 1),
('Bacon Lover XXL', 8900.0, 'Triple carne, bacon ahumado en tiras gruesas, cheddar y alioli de ajo asado', 20, 'bacon_xxl.png', TRUE, FALSE, CURRENT_TIMESTAMP, 1),

-- Papas & Acompañamientos (Cat 2)
('Papas Cheddar & Bacon', 4500.0, 'Papas bastón doble fritura con lluvia de cheddar caliente y panceta crocante', 40, 'papas_cheddar.png', TRUE, FALSE, CURRENT_TIMESTAMP, 2),
('Bastones de Muzzarella', 5200.0, '6 bastones gigantes rebozados en panko con salsa de tomates rústicos', 30, 'muzza_sticks.png', TRUE, FALSE, CURRENT_TIMESTAMP, 2),

-- Pizzas de Masa Madre (Cat 3)
('Muzzarella Especial', 9500.0, 'Fior di latte, aceitunas negras, orégano patagónico y oliva virgen', 15, 'pizza_muzza.png', TRUE, FALSE, CURRENT_TIMESTAMP, 3),
('La Fugazzeta Rellena', 11200.0, 'Cebolla caramelizada a fuego lento, doble muzzarella y provolone gratinado', 12, 'fugazzeta.png', TRUE, FALSE, CURRENT_TIMESTAMP, 3),

-- Empanadas Criollas (Cat 4)
('Matambre a Cuchillo', 1300.0, 'Cortada a mano con morrón asado, verdeo y comino justo', 60, 'emp_carne.png', TRUE, FALSE, CURRENT_TIMESTAMP, 4),
('Jamón y Queso Ahumado', 1200.0, 'Queso tybo fundido con jamón natural y toque de orégano fresco', 50, 'emp_jyq.png', TRUE, FALSE, CURRENT_TIMESTAMP, 4),

-- Sándwiches & Lomos (Cat 5)
('Lomo Completo Criollo', 10500.0, 'Lomo tierno cortado en finas tiras, lechuga, tomate, jamón, queso y huevo frito en pan de Viena', 18, 'lomo_completo.png', TRUE, FALSE, CURRENT_TIMESTAMP, 5),
('Sándwich de Bondiola Braiaseada', 9800.0, 'Bondiola desmenuzada en cocción lenta de 6 horas con barbacoa casera y coleslaw', 15, 'bondiola_sand.png', TRUE, FALSE, CURRENT_TIMESTAMP, 5),

-- Ensaladas Frescas (Cat 6)
('Ensalada Caesar Club', 7200.0, 'Lechuga capelina, pollo crocante especiado, croutons de campo, parmesano y aderezo caesar artesanal', 22, 'caesar.png', TRUE, FALSE, CURRENT_TIMESTAMP, 6),
('Ensalada Mediteránea', 6800.0, 'Mix de verdes, tomates cherry confitados, queso feta, aceitunas negras y tiras de zucchini grillado', 20, 'mediterranea.png', TRUE, FALSE, CURRENT_TIMESTAMP, 6),

-- Bebidas & Cervezas (Cat 7)
('Cerveza IPA Artesanal (Pinta)', 3500.0, 'Rubia dorada refrescante, notas cítricas y amargor equilibrado (500ml)', 50, 'ipa.png', TRUE, FALSE, CURRENT_TIMESTAMP, 7),
('Limonada con Menta y Jengibre', 2800.0, 'Exprimir natural de limones frescos, hojas de menta orgánica y jengibre rallado (1 Litro)', 40, 'limonada.png', TRUE, FALSE, CURRENT_TIMESTAMP, 7),

-- Postres Caseros (Cat 8)
('Volcán de Chocolate', 5500.0, 'Torta tibiecita de chocolate amargo con corazón líquido y bocha de helado de americana', 14, 'volcan.png', TRUE, FALSE, CURRENT_TIMESTAMP, 8),
('Cheesecake de Frutos Rojos', 5200.0, 'Base de galletitas trituradas, crema suave de queso y reducción de frutos rojos naturales', 16, 'cheesecake.png', TRUE, FALSE, CURRENT_TIMESTAMP, 8),

-- Pizzas Vegetarianas (Cat 9)
('Pizza de Vegetales Asados', 10200.0, 'Berenjenas, morrones, zucchini a la parrilla, ajos confitados y muzzarella', 15, 'pizza_veggie.png', TRUE, FALSE, CURRENT_TIMESTAMP, 9),

-- Promos del Chef (Cat 10)
('Combo Duo Poderoso', 15500.0, '2 Burgers Smash + 1 Papas grandes con cheddar + 2 Bebidas a elección', 25, 'combo_duo.png', TRUE, FALSE, CURRENT_TIMESTAMP, 10),
('Combo Familiar Pizzero', 18500.0, '1 Pizza Muzzarella + 1 Pizza Especial + 6 Empanadas de carne + 1 Gaseosa 1.5L', 10, 'combo_familiar.png', TRUE, FALSE, CURRENT_TIMESTAMP, 10),
('Picada Callejera para 4', 16200.0, 'Bastones de muzzarella, papas fritas, mini empanadas, aros de cebolla y salsas dip', 12, 'picada.png', TRUE, FALSE, CURRENT_TIMESTAMP, 10);

COMMIT;