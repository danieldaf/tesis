
# El servicio android (Activity) es referenciado aqui mediante la calse 'backend' see @EstadisticasActivity.load()

chartEficiencia = {}
charts = []

procesarInfoGeneral = () ->
	info = JSON.parse(backend.recuperarInfoEjecucion())
	txt  = 'Fecha Inicio: \n\t'+info.fechaInicio
	txt  += '\nFecha Fin: \n\t'+info.fechaFin
	txt  += '\nDuración: \n\t'+info.duracionMS
	txt  += '\nInfo del dispositivo: \n\t'+info.deviceInfo
	#txt += '\nAlgoritmos: \n'+info.algoritmos
	txt += '\n\nCant Imágenes procesadas: '+info.cantImagenes
	txt += '\nCant Transformaciones aplicadas: '+info.cantTransformaciones
	txt += '\nCant Algoritmos evaluados: '+info.cantAlgoritmos	
	d3.select('#info_texto').text(txt)
	info
	
procesarDatosEficiencia = () ->
	grafico = JSON.parse(backend.recuperarDatosEficiencia()) 
	chartEficiencia = barHChart()
		.idTagInsert(grafico.idTag)
		.title(grafico.titulo)
		.metricUnit(grafico.unidadMetrica)
		.labelAxisX(grafico.labelEjeX)
		.domainX(grafico.ticksEjeX)
		.semanticColors(grafico.itemLabels)
		.colors(grafico.itemColors)
		.width(grafico.width)
		.height(grafico.height)
		.build()
	grafico

procesarSeriesEficiencia = () ->
	series = JSON.parse(backend.recuperarSeriesEficiencia())
	chartEficiencia.updateSeries(series)
	series	

procesarGrafico = (grafico) ->
	chart = lineChart()
		.idTagInsert(grafico.idTag)
		.title(grafico.titulo)
		.labelAxisX(grafico.labelEjeX)
		.labelAxisY(grafico.labelEjeY)
		.domainX(grafico.ticksEjeX)
		.domainY(grafico.ticksEjeY)
		.width(grafico.width)
		.height(grafico.height)
		.build()
	charts = charts.concat [chart]
	grafico

procesarListadoGraficos = () ->
	infoGraficos = JSON.parse(backend.recuperarListadoGraficos())
	procesarGrafico infoGrafico for infoGrafico in infoGraficos
	infoGraficos
	
procesarSeriesGrafico = (chart) ->
	series = JSON.parse(backend.recuperarSeriesGrafico(chart.idTagInsert()))
	chart.updateSeries(series)
	chart

procesarSeriesGaficos = () ->
	procesarSeriesGrafico chart for chart in charts
	charts
	
buscarCharts = () ->
	#svgCharts = d3.selectAll("div .chart")[0]
	#strSvg = ""
	#strSvg += svgChart.innerHTML for svgChart in svgCharts
	svgDelimiter = backend.getSVGDelimiter()
	strSvg = exportAllSVG(svgDelimiter)
	backend.asentarGraficosAExportar(strSvg)
	svgCharts
		
procesarInfoGeneral()
procesarDatosEficiencia()
procesarListadoGraficos()
procesarSeriesEficiencia()
procesarSeriesGaficos()
buscarCharts()

#d3.select('#debug_info').text(charts)
