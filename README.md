HISTORIAS DE USUARIO
Historias de Usuario – Usuario Invitado
Consulta de móviles por marca


 Como usuario invitado,
 quiero seleccionar una marca de móvil,
 para ver una lista de móviles de esa marca con información resumida.



Consulta de móviles por precio


 Como usuario invitado,
 quiero filtrar móviles por precio mínimo y máximo,
 para ver solo los móviles dentro de mi rango de presupuesto.



Filtrado avanzado por RAM


 Como usuario invitado,
 quiero filtrar móviles por cantidad de RAM (mínimo y máximo),
 para encontrar dispositivos con el rendimiento que necesito.



Filtrado por NFC


 Como usuario invitado,
 quiero filtrar móviles que tengan o no NFC,
 para saber si un móvil es compatible con pagos móviles u otras funciones.



Filtrado por tecnología de pantalla


 Como usuario invitado,
 quiero filtrar móviles según la tecnología de pantalla,
 para elegir el móvil con la pantalla que prefiero.



Vista de información resumida de móviles


 Como usuario invitado,
 quiero ver información resumida de los móviles en los resultados de búsqueda,
 para compararlos rápidamente sin necesidad de abrir cada ficha completa.



Vista de detalles de un móvil específico


 Como usuario invitado,
 quiero hacer clic en un móvil de la lista para ver todas sus características,
 para tomar una decisión de compra más informada.



Comparativa de dos móviles


 Como usuario invitado,
 quiero seleccionar dos móviles para compararlos lado a lado,
 para analizar sus características y diferencias de manera clara.



Página de móviles en tendencia


 Como usuario invitado,
 quiero ver los cinco móviles más consultados en la página principal con información resumida,
 para identificar rápidamente los móviles populares del momento.




Historias de Usuario – Administrador
CRUD de móviles


 Como administrador,
 quiero crear, leer, actualizar y eliminar móviles en el sistema,
 para mantener la información de la base de datos siempre actualizada y completa.



Gestión de características de móviles


 Como administrador,
 quiero poder definir o modificar todas las características de un móvil (marca, modelo, procesador, almacenamiento, RAM, pantalla, dimensiones, peso, cámara, batería, NFC, precio, fecha de lanzamiento),
 para asegurarme de que la información mostrada a los usuarios sea correcta y detallada.



Visualización de móviles populares


 Como administrador,
 quiero ver qué móviles son más consultados por los usuarios,
 para destacar los más populares en la página principal.

2. DIAGRAMAS DE CASOS DE USO 

1. Actor: Usuario Invitado
Este actor representa al usuario externo que interactúa con el catálogo. Sus casos de uso se centran en la búsqueda, filtrado y visualización.
CU01: Consultar móviles por marca.
CU02: Filtrar móviles por precio (Incluye establecer rango mín/máx).
CU03: Filtrar móviles por RAM (Incluye establecer rango mín/máx).
CU04: Filtrar móviles por NFC.
CU05: Filtrar móviles por tecnología de pantalla.
CU06: Visualizar resumen de móviles (Este suele ser un resultado implícito de las búsquedas).
CU07: Ver detalles de un móvil (Acceso a la ficha técnica completa).
CU08: Comparar móviles (Selección y vista lado a lado).
CU09: Visualizar móviles en tendencia (Vista de los 5 más populares).

2. Actor: Administrador
Este actor tiene permisos de gestión sobre los datos y la configuración del sistema.
CU10: Gestionar móviles (CRUD):
Crear móvil.
Modificar móvil.
Eliminar móvil.
Consultar móvil.
CU11: Gestionar características técnicas: (Poder editar campos específicos como procesador, cámara, batería, etc.).
CU12: Consultar estadísticas de popularidad: (Ver cuáles son los móviles más visitados para gestionar la sección de tendencias).




url diagrama de caso de uso 


