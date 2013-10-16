-- Porcentaje de matches correctos por cada transformacion
-- La idea es mostrar como varia porcentualmente la eficiencia de matching de los algoritmos.
-- Hay que implementar una consulta por cada transformacion.
-- Eje x=step de la transformacion
-- Eje y=porcentaje de matches

select E._id, A.nombre as Algoritmo, T.nombre as Transformacion, count(E.percent_of_matches) as "Count_Matches",
sum(E.percent_of_matches) as "Percent_Acum", sum(E.correct_matches_percent) as "Percent_Correct_Acum", E.argument_value as "Paso_Transformacion"
from estadistica as E
left join algoritmo as A on (A._id=E.id_algoritmo)
left join transformacion as T on (T._id=E.id_transformacion)
group by T.nombre, A.nombre, E.argument_value
order by T.nombre, A.nombre, E.argument_value;

-- left join imagen_transformada as IT on (IT._id=E.id_imagen_transformada)

-- Columnas resultantes:
-- Algoritmo: nombre de los algoritmos empleados (serie)
-- Transformacion: nombre de la transformacion aplicada (grafico)
-- Count_Matches: cantidad de coincidencias encontradas
-- Percent_Acum: porcentaje de coincidencias 
-- Percent_Correct_Acum: porcentaje de coincidencias correctas (eje Y)
-- Paso_Transformacion: Paso de la alteracion de la transformacion (eje X)
------------------------------------------------------

-- Eficiencia en tiempo de los algoritmos

select A.nombre as Algoritmo, 
sum(E.total_keypoints) as "TotalKeypoints",
sum(E.consumed_time_ms_detector) as "TimeDetector",
sum(E.consumed_time_ms_extractor) as "TimeExtractor",
sum(E.consumed_time_ms_matcher) as "TimeMatcher",
sum(E.consumed_time_ms) as "TimeTotal",
count(E._id) as "CountEstadisticas"
from estadistica as E
left join algoritmo as A on (A._id=E.id_algoritmo)
group by A.nombre;

(E.consumed_time_ms_detector/E.total_keypoints1) as "TiempoDeteccionPorKeypoints",
(E.consumed_time_ms_extractor/E.total_keypoints) as "TiempoExtraccionPorKeypoints",
((E.consumed_time_ms_detector+E.consumed_time_ms_extractor)/E.total_keypoints) as "TiempoDetExtPorKeypoints",
(E.consumed_time_ms/E.total_keypoints) as "TiempoTotalPorKeypoints",
-- a/b + x/y
-- (a*y+x*b)/(b*y)

-- Numeros de puntos detectados
-- Tiempo de deteccion
-- Tiempo de extraccion
-- Tiempo total
-- Tiempo por puntos


------------------------------------------------------

-- Tiempo que tardo en procesar cada transformacion por algoritmo
-- La idea es mostrar que algoritmos es mas eficiente para cada transformacion
-- Eje x= cantidad de caracteristicas
-- Eje y= tiempo de extraccion de las caracteristicas

-- NO EMPLEADA AUN

select A.nombre as Algoritmo, sum(E.consumed_time_ms) as Consumed_Time_Acum, 
count(E.consumed_time_ms) as Count_Consumed_Time
from estadistica as E
left join algoritmo as A on (A._id=E.id_algoritmo)
group by E.id_algoritmo
;