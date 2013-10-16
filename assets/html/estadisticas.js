// Generated by CoffeeScript 1.6.3
var buscarCharts, chartEficiencia, charts, procesarDatosEficiencia, procesarGrafico, procesarInfoGeneral, procesarListadoGraficos, procesarSeriesEficiencia, procesarSeriesGaficos, procesarSeriesGrafico;

chartEficiencia = {};

charts = [];

procesarInfoGeneral = function() {
  var info, txt;
  info = JSON.parse(backend.recuperarInfoEjecucion());
  txt = 'Fecha Inicio: \n\t' + info.fechaInicio;
  txt += '\nFecha Fin: \n\t' + info.fechaFin;
  txt += '\nDuración: \n\t' + info.duracionMS;
  txt += '\nInfo del dispositivo: \n\t' + info.deviceInfo;
  txt += '\n\nCant Imágenes procesadas: ' + info.cantImagenes;
  txt += '\nCant Transformaciones aplicadas: ' + info.cantTransformaciones;
  txt += '\nCant Algoritmos evaluados: ' + info.cantAlgoritmos;
  d3.select('#info_texto').text(txt);
  return info;
};

procesarDatosEficiencia = function() {
  var grafico;
  grafico = JSON.parse(backend.recuperarDatosEficiencia());
  chartEficiencia = barHChart().idTagInsert(grafico.idTag).title(grafico.titulo).metricUnit(grafico.unidadMetrica).labelAxisX(grafico.labelEjeX).domainX(grafico.ticksEjeX).semanticColors(grafico.itemLabels).colors(grafico.itemColors).width(grafico.width).height(grafico.height).build();
  return grafico;
};

procesarSeriesEficiencia = function() {
  var series;
  series = JSON.parse(backend.recuperarSeriesEficiencia());
  chartEficiencia.updateSeries(series);
  return series;
};

procesarGrafico = function(grafico) {
  var chart;
  chart = lineChart().idTagInsert(grafico.idTag).title(grafico.titulo).labelAxisX(grafico.labelEjeX).labelAxisY(grafico.labelEjeY).domainX(grafico.ticksEjeX).domainY(grafico.ticksEjeY).width(grafico.width).height(grafico.height).build();
  charts = charts.concat([chart]);
  return grafico;
};

procesarListadoGraficos = function() {
  var infoGrafico, infoGraficos, _i, _len;
  infoGraficos = JSON.parse(backend.recuperarListadoGraficos());
  for (_i = 0, _len = infoGraficos.length; _i < _len; _i++) {
    infoGrafico = infoGraficos[_i];
    procesarGrafico(infoGrafico);
  }
  return infoGraficos;
};

procesarSeriesGrafico = function(chart) {
  var series;
  series = JSON.parse(backend.recuperarSeriesGrafico(chart.idTagInsert()));
  chart.updateSeries(series);
  return chart;
};

procesarSeriesGaficos = function() {
  var chart, _i, _len;
  for (_i = 0, _len = charts.length; _i < _len; _i++) {
    chart = charts[_i];
    procesarSeriesGrafico(chart);
  }
  return charts;
};

buscarCharts = function() {
  var strSvg, svgDelimiter;
  svgDelimiter = backend.getSVGDelimiter();
  strSvg = exportAllSVG(svgDelimiter);
  backend.asentarGraficosAExportar(strSvg);
  return svgCharts;
};

procesarInfoGeneral();

procesarDatosEficiencia();

procesarListadoGraficos();

procesarSeriesEficiencia();

procesarSeriesGaficos();

buscarCharts();