create table parametro (
	nombre text not null primary key,
	valor text
);

create table imagen_origen (
	_id integer not null primary key,
	nombre text not null,
	uri_original not null,
	uri text
);

create table algoritmo (
	_id integer not null primary key,
	nombre text not null,
	nombre_detector text not null,
	nombre_extractor text not null,
	nombre_matcher text not null
);

create table transformacion (
	_id integer not null primary key,
	nombre text not null,
	json_caracteristicas text
);

create table imagen_transformada (
	_id integer not null primary key,
	id_imagen_origen integer not null,
	id_transformacion integer not null,
	json_caracteristicas text,
	uri text
);

create table estadistica (
	_id integer  not null primary key,
	id_algoritmo integer not null,
	id_transformacion integer not null,
	id_imagen_origen integer not null,
	id_imagen_transformada integer not null,

	total_keypoints integer,
	argument_value real,
	percent_of_matches real,
	ratio_test_false_level real,
	mean_distance real,
	std_dev_distance real,
	correct_matches_percent real,
	homography_error real,
	consumed_time_ms real,
	consumed_time_ms_detector real,
	consumed_time_ms_extractor real,
	consumed_time_ms_matcher real,
	distancia_media_proyecciones real,
	desviacion_estandar_proyecciones real,
	distancia_minima_proyecciones real,
	distancia_maxima_proyecciones real,
	is_valid integer
);

create table source_keypoint (
	_id integer not null primary key,
	id_estadistica integer not null,
	
	x real,
	y real,
	size real,
	angle real,
	response real,
	octave integer,
	class_id integer
);

create table transformed_keypoint (
	_id integer not null primary key,
	id_estadistica integer not null,
	
	x real,
	y real,
	size real,
	angle real,
	response real,
	octave integer,
	class_id integer
);

insert into parametro (nombre, valor) values ('DB_VERSION', '1');
