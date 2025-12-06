package com.sibelsama.lovelyy5.data

/**
 * Datos completos de regiones y comunas de Chile
 * Unificado de RegionsData.kt y ChileanRegionsData.kt
 */
object RegionsData {

    data class Region(
        val name: String,
        val comunas: List<String>
    )

    // Lista completa de regiones con sus comunas
    val regions = listOf(
        Region(
            name = "Región de Arica y Parinacota",
            comunas = listOf("Arica", "Camarones", "General Lagos", "Putre")
        ),
        Region(
            name = "Región de Tarapacá",
            comunas = listOf("Alto Hospicio", "Camiña", "Colchane", "Huara", "Iquique", "Pica", "Pozo Almonte")
        ),
        Region(
            name = "Región de Antofagasta",
            comunas = listOf("Antofagasta", "Calama", "María Elena", "Mejillones", "Ollagüe", "San Pedro de Atacama", "Sierra Gorda", "Taltal", "Tocopilla")
        ),
        Region(
            name = "Región de Atacama",
            comunas = listOf("Alto del Carmen", "Caldera", "Chañaral", "Copiapó", "Diego de Almagro", "Freirina", "Huasco", "Tierra Amarilla", "Vallenar")
        ),
        Region(
            name = "Región de Coquimbo",
            comunas = listOf("Andacollo", "Canela", "Combarbalá", "Coquimbo", "Illapel", "La Higuera", "La Serena", "Los Vilos", "Monte Patria", "Ovalle", "Paiguano", "Punitaqui", "Río Hurtado", "Salamanca", "Vicuña")
        ),
        Region(
            name = "Región de Valparaíso",
            comunas = listOf("Algarrobo", "Cabildo", "Calera", "Calle Larga", "Cartagena", "Casablanca", "Catemu", "Concón", "El Quisco", "El Tabo", "Hijuelas", "Isla de Pascua", "Juan Fernández", "La Cruz", "La Ligua", "Limache", "Llaillay", "Los Andes", "Nogales", "Olmué", "Panquehue", "Papudo", "Petorca", "Puchuncaví", "Putaendo", "Quillota", "Quilpué", "Quintero", "Rinconada", "San Antonio", "San Esteban", "San Felipe", "Santa María", "Santo Domingo", "Valparaíso", "Villa Alemana", "Viña del Mar", "Zapallar")
        ),
        Region(
            name = "Región Metropolitana",
            comunas = listOf("Alhué", "Buin", "Calera de Tango", "Cerrillos", "Cerro Navia", "Colina", "Conchalí", "Curacaví", "El Bosque", "El Monte", "Estación Central", "Huechuraba", "Independencia", "Isla de Maipo", "La Cisterna", "La Florida", "La Granja", "La Pintana", "La Reina", "Lampa", "Las Condes", "Lo Barnechea", "Lo Espejo", "Lo Prado", "Macul", "Maipú", "María Pinto", "Melipilla", "Ñuñoa", "Padre Hurtado", "Paine", "Pedro Aguirre Cerda", "Peñaflor", "Peñalolén", "Pirque", "Providencia", "Pudahuel", "Puente Alto", "Quilicura", "Quinta Normal", "Recoleta", "Renca", "San Bernardo", "San Joaquín", "San José de Maipo", "San Miguel", "San Pedro", "San Ramón", "Santiago", "Talagante", "Tiltil", "Vitacura", "Yasna")
        ),
        Region(
            name = "Región del Libertador Bernardo O'Higgins",
            comunas = listOf("Chépica", "Chimbarongo", "Codegua", "Coinco", "Coltauco", "Doñihue", "Graneros", "La Estrella", "Las Cabras", "Litueche", "Lolol", "Machalí", "Malloa", "Marchihue", "Mostazal", "Nancagua", "Navidad", "Olivar", "Palmilla", "Paredones", "Peralillo", "Peumo", "Pichidegua", "Pichilemu", "Placilla", "Pumanque", "Quinta de Tilcoco", "Rancagua", "Rengo", "Requínoa", "San Fernando", "San Vicente de Tagua Tagua", "Santa Cruz")
        ),
        Region(
            name = "Región del Maule",
            comunas = listOf("Cauquenes", "Chanco", "Colbún", "Constitución", "Curepto", "Curicó", "Empedrado", "Hualañé", "Licantén", "Linares", "Longaví", "Maule", "Molina", "Parral", "Pelarco", "Pelluhue", "Pencahue", "Rauco", "Retiro", "Río Claro", "Romeral", "Sagrada Familia", "San Clemente", "San Javier", "San Rafael", "Talca", "Teno", "Vichuquén", "Villa Alegre", "Yerbas Buenas")
        ),
        Region(
            name = "Región del Ñuble",
            comunas = listOf("Bulnes", "Chillán", "Chillán Viejo", "Cobquecura", "Coelemu", "Coihueco", "El Carmen", "Ninhue", "Ñiquén", "Pemuco", "Pinto", "Portezuelo", "Quillón", "Quirihue", "Ránquil", "San Carlos", "San Fabián", "San Ignacio", "San Nicolás", "Treguaco", "Yungay")
        ),
        Region(
            name = "Región del Biobío",
            comunas = listOf("Alto Biobío", "Antuco", "Arauco", "Cabrero", "Cañete", "Chiguayante", "Concepción", "Contulmo", "Coronel", "Curanilahue", "Florida", "Hualpén", "Hualqui", "Laja", "Lebu", "Los Álamos", "Los Ángeles", "Lota", "Mulchén", "Nacimiento", "Negrete", "Penco", "Quilaco", "Quilleco", "San Pedro de la Paz", "San Rosendo", "Santa Bárbara", "Santa Juana", "Talcahuano", "Tirúa", "Tomé", "Tucapel", "Yumbel")
        ),
        Region(
            name = "Región de La Araucanía",
            comunas = listOf("Angol", "Carahue", "Cholchol", "Collipulli", "Cunco", "Curacautín", "Curarrehue", "Ercilla", "Freire", "Galvarino", "Gorbea", "Lautaro", "Loncoche", "Lonquimay", "Los Sauces", "Lumaco", "Melipeuco", "Nueva Imperial", "Padre Las Casas", "Perquenco", "Pitrufquén", "Pucón", "Purén", "Renaico", "Saavedra", "Temuco", "Teodoro Schmidt", "Toltén", "Traiguén", "Victoria", "Vilcún", "Villarrica")
        ),
        Region(
            name = "Región de Los Ríos",
            comunas = listOf("Corral", "Futrono", "La Unión", "Lago Ranco", "Lanco", "Los Lagos", "Máfil", "Mariquina", "Paillaco", "Panguipulli", "Río Bueno", "Valdivia")
        ),
        Region(
            name = "Región de Los Lagos",
            comunas = listOf("Ancud", "Calbuco", "Castro", "Chaitén", "Chonchi", "Cochamó", "Curaco de Vélez", "Dalcahue", "Fresia", "Frutillar", "Futaleufú", "Hualaihué", "Llanquihue", "Los Muermos", "Maullín", "Osorno", "Palena", "Puerto Montt", "Puerto Octay", "Puerto Varas", "Puqueldón", "Purranque", "Puyehue", "Queilén", "Quellon", "Quemchi", "Quinchao", "Río Negro", "San Juan de la Costa", "San Pablo")
        ),
        Region(
            name = "Región Aysén del General Carlos Ibáñez del Campo",
            comunas = listOf("Aysén", "Chile Chico", "Cisnes", "Cochrane", "Coyhaique", "Guaitecas", "Lago Verde", "O'Higgins", "Río Ibáñez", "Tortel")
        ),
        Region(
            name = "Región de Magallanes y de la Antártica Chilena",
            comunas = listOf("Antártica", "Cabo de Hornos", "Laguna Blanca", "Natales", "Porvenir", "Primavera", "Punta Arenas", "Río Verde", "San Gregorio", "Timaukel", "Torres del Paine")
        )
    )

    // Mantener compatibilidad con código existente
    val REGIONS = listOf(
        "XV - Región de Arica y Parinacota",
        "I - Región de Tarapacá",
        "II - Región de Antofagasta",
        "III - Región de Atacama",
        "IV - Región de Coquimbo",
        "V - Región de Valparaíso",
        "RM - Región Metropolitana",
        "VI - Región del Libertador Bernardo O'Higgins",
        "VII - Región del Maule",
        "XVI - Región de Ñuble",
        "VIII - Región del Biobío",
        "IX - Región de La Araucanía",
        "XIV - Región de Los Ríos",
        "X - Región de Los Lagos",
        "XI - Región Aysén del General Carlos Ibáñez del Campo",
        "XII - Región de Magallanes y de la Antártica Chilena"
    )

    // Mapa para compatibilidad con código existente
    val COMUNAS_BY_REGION: Map<String, List<String>> = mapOf(
        "XV - Región de Arica y Parinacota" to listOf("Arica", "Camarones", "General Lagos", "Putre"),
        "I - Región de Tarapacá" to listOf("Alto Hospicio", "Camiña", "Colchane", "Huara", "Iquique", "Pica", "Pozo Almonte"),
        "II - Región de Antofagasta" to listOf("Antofagasta", "Calama", "María Elena", "Mejillones", "Ollagüe", "San Pedro de Atacama", "Sierra Gorda", "Taltal", "Tocopilla"),
        "III - Región de Atacama" to listOf("Alto del Carmen", "Caldera", "Chañaral", "Copiapó", "Diego de Almagro", "Freirina", "Huasco", "Tierra Amarilla", "Vallenar"),
        "IV - Región de Coquimbo" to listOf("Andacollo", "Canela", "Combarbalá", "Coquimbo", "Illapel", "La Higuera", "La Serena", "Los Vilos", "Monte Patria", "Ovalle", "Paiguano", "Punitaqui", "Río Hurtado", "Salamanca", "Vicuña"),
        "V - Región de Valparaíso" to listOf("Algarrobo", "Cabildo", "Calera", "Calle Larga", "Cartagena", "Casablanca", "Catemu", "Concón", "El Quisco", "El Tabo", "Hijuelas", "Isla de Pascua", "Juan Fernández", "La Cruz", "La Ligua", "Limache", "Llaillay", "Los Andes", "Nogales", "Olmué", "Panquehue", "Papudo", "Petorca", "Puchuncaví", "Putaendo", "Quillota", "Quilpué", "Quintero", "Rinconada", "San Antonio", "San Esteban", "San Felipe", "Santa María", "Santo Domingo", "Valparaíso", "Villa Alemana", "Viña del Mar", "Zapallar"),
        "RM - Región Metropolitana" to listOf("Alhué", "Buin", "Calera de Tango", "Cerrillos", "Cerro Navia", "Colina", "Conchalí", "Curacaví", "El Bosque", "El Monte", "Estación Central", "Huechuraba", "Independencia", "Isla de Maipo", "La Cisterna", "La Florida", "La Granja", "La Pintana", "La Reina", "Lampa", "Las Condes", "Lo Barnechea", "Lo Espejo", "Lo Prado", "Macul", "Maipú", "María Pinto", "Melipilla", "Ñuñoa", "Padre Hurtado", "Paine", "Pedro Aguirre Cerda", "Peñaflor", "Peñalolén", "Pirque", "Providencia", "Pudahuel", "Puente Alto", "Quilicura", "Quinta Normal", "Recoleta", "Renca", "San Bernardo", "San Joaquín", "San José de Maipo", "San Miguel", "San Pedro", "San Ramón", "Santiago", "Talagante", "Tiltil", "Vitacura", "Yasna"),
        "VI - Región del Libertador Bernardo O'Higgins" to listOf("Chépica", "Chimbarongo", "Codegua", "Coinco", "Coltauco", "Doñihue", "Graneros", "La Estrella", "Las Cabras", "Litueche", "Lolol", "Machalí", "Malloa", "Marchihue", "Mostazal", "Nancagua", "Navidad", "Olivar", "Palmilla", "Paredones", "Peralillo", "Peumo", "Pichidegua", "Pichilemu", "Placilla", "Pumanque", "Quinta de Tilcoco", "Rancagua", "Rengo", "Requínoa", "San Fernando", "San Vicente de Tagua Tagua", "Santa Cruz"),
        "VII - Región del Maule" to listOf("Cauquenes", "Chanco", "Colbún", "Constitución", "Curepto", "Curicó", "Empedrado", "Hualañé", "Licantén", "Linares", "Longaví", "Maule", "Molina", "Parral", "Pelarco", "Pelluhue", "Pencahue", "Rauco", "Retiro", "Río Claro", "Romeral", "Sagrada Familia", "San Clemente", "San Javier", "San Rafael", "Talca", "Teno", "Vichuquén", "Villa Alegre", "Yerbas Buenas"),
        "XVI - Región de Ñuble" to listOf("Bulnes", "Chillán", "Chillán Viejo", "Cobquecura", "Coelemu", "Coihueco", "El Carmen", "Ninhue", "Ñiquén", "Pemuco", "Pinto", "Portezuelo", "Quillón", "Quirihue", "Ránquil", "San Carlos", "San Fabián", "San Ignacio", "San Nicolás", "Treguaco", "Yungay"),
        "VIII - Región del Biobío" to listOf("Alto Biobío", "Antuco", "Arauco", "Cabrero", "Cañete", "Chiguayante", "Concepción", "Contulmo", "Coronel", "Curanilahue", "Florida", "Hualpén", "Hualqui", "Laja", "Lebu", "Los Álamos", "Los Ángeles", "Lota", "Mulchén", "Nacimiento", "Negrete", "Penco", "Quilaco", "Quilleco", "San Pedro de la Paz", "San Rosendo", "Santa Bárbara", "Santa Juana", "Talcahuano", "Tirúa", "Tomé", "Tucapel", "Yumbel"),
        "IX - Región de La Araucanía" to listOf("Angol", "Carahue", "Cholchol", "Collipulli", "Cunco", "Curacautín", "Curarrehue", "Ercilla", "Freire", "Galvarino", "Gorbea", "Lautaro", "Loncoche", "Lonquimay", "Los Sauces", "Lumaco", "Melipeuco", "Nueva Imperial", "Padre Las Casas", "Perquenco", "Pitrufquén", "Pucón", "Purén", "Renaico", "Saavedra", "Temuco", "Teodoro Schmidt", "Toltén", "Traiguén", "Victoria", "Vilcún", "Villarrica"),
        "XIV - Región de Los Ríos" to listOf("Corral", "Futrono", "La Unión", "Lago Ranco", "Lanco", "Los Lagos", "Máfil", "Mariquina", "Paillaco", "Panguipulli", "Río Bueno", "Valdivia"),
        "X - Región de Los Lagos" to listOf("Ancud", "Calbuco", "Castro", "Chaitén", "Chonchi", "Cochamó", "Curaco de Vélez", "Dalcahue", "Fresia", "Frutillar", "Futaleufú", "Hualaihué", "Llanquihue", "Los Muermos", "Maullín", "Osorno", "Palena", "Puerto Montt", "Puerto Octay", "Puerto Varas", "Puqueldón", "Purranque", "Puyehue", "Queilén", "Quellon", "Quemchi", "Quinchao", "Río Negro", "San Juan de la Costa", "San Pablo"),
        "XI - Región Aysén del General Carlos Ibáñez del Campo" to listOf("Aysén", "Chile Chico", "Cisnes", "Cochrane", "Coyhaique", "Guaitecas", "Lago Verde", "O'Higgins", "Río Ibáñez", "Tortel"),
        "XII - Región de Magallanes y de la Antártica Chilena" to listOf("Antártica", "Cabo de Hornos", "Laguna Blanca", "Natales", "Porvenir", "Primavera", "Punta Arenas", "Río Verde", "San Gregorio", "Timaukel", "Torres del Paine")
    )

    /**
     * Obtiene lista de nombres de regiones
     */
    fun getRegionNames(): List<String> {
        return regions.map { it.name }
    }

    /**
     * Obtiene comunas de una región específica
     */
    fun getComunasByRegion(regionName: String): List<String> {
        return regions.find { it.name == regionName }?.comunas ?: emptyList()
    }

    /**
     * Busca la región de una comuna
     */
    fun getRegionByComuna(comunaName: String): String? {
        return regions.find { region ->
            region.comunas.any { it.equals(comunaName, ignoreCase = true) }
        }?.name
    }

    /**
     * Convierte nombre de región nuevo al formato anterior para compatibilidad
     */
    fun getOldFormatRegionName(newFormatName: String): String? {
        val mapping = mapOf(
            "Región de Arica y Parinacota" to "XV - Región de Arica y Parinacota",
            "Región de Tarapacá" to "I - Región de Tarapacá",
            "Región de Antofagasta" to "II - Región de Antofagasta",
            "Región de Atacama" to "III - Región de Atacama",
            "Región de Coquimbo" to "IV - Región de Coquimbo",
            "Región de Valparaíso" to "V - Región de Valparaíso",
            "Región Metropolitana" to "RM - Región Metropolitana",
            "Región del Libertador Bernardo O'Higgins" to "VI - Región del Libertador Bernardo O'Higgins",
            "Región del Maule" to "VII - Región del Maule",
            "Región de Ñuble" to "XVI - Región de Ñuble",
            "Región del Biobío" to "VIII - Región del Biobío",
            "Región de La Araucanía" to "IX - Región de La Araucanía",
            "Región de Los Ríos" to "XIV - Región de Los Ríos",
            "Región de Los Lagos" to "X - Región de Los Lagos",
            "Región Aysén del General Carlos Ibáñez del Campo" to "XI - Región Aysén del General Carlos Ibáñez del Campo",
            "Región de Magallanes y de la Antártica Chilena" to "XII - Región de Magallanes y de la Antártica Chilena"
        )
        return mapping[newFormatName]
    }
}
