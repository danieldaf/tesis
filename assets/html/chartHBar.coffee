# http://bost.ocks.org/mike/chart/
	
barHChart = () ->

	result = () ->

	result.updateBars = () ->
	
	result.updateSeries = (datosSeries) ->
		_y  = d3.scale.linear().domain([0, datosSeries.length]).range [height, 0]
		_y2 = d3.scale.linear().domain([0, colors.length+1]).range [0, -_y(1)+_y(0)]
	
		texts = _svgData.selectAll("text")
			.data(datosSeries, (d) -> d.name)
		
		#update
		#append
		texts.enter().append("text")
			.attr("x", "-1ex")
			.attr("y", (d,i) -> _y(i+0.5))
			.attr("text-anchor", "end")
			.text((d) -> d.name)
		#update + append
		#remove
		
		barDatos = []
		for barDato in datosSeries
			for dato in barDato.data
				item = 
					name: barDato.name+":"+dato
					data: dato
				barDatos = barDatos.concat item
			item =
				name: barDato.name+":null"
				data: -1
			barDatos = barDatos.concat item

		bars = _svgData.selectAll("path")
			.data(barDatos, (d) -> d.name)
	
		#update
		#append
		bars.enter().append("path")
			.attr("stroke", (d) -> if d.data == -1 then "transparent" else "black")
			.attr("fill", (d, i) -> if d.data == -1 then "transparent" else colors[i % (colors.length+1)])
			.attr("stroke-width", 1)
			.attr("d", (d, i) ->
				x1 = 0
				y1 = height-_y2(i+0.5)-2
				if d.data == -1
					x2 = 0
				else 
					x2 = _x(d.data) 
				y2 = height-_y2(i+1.5)+2
				a = "M"+x1+","+y1+"L"+x2+","+y1+"L"+x2+","+y2+"L"+x1+","+y2+"L"+x1+","+y1+"z"
				)
		#update + append
		#remove
		
		valores = _svgData.selectAll("text .textValor")
			.data(barDatos, (d) -> d.name)
		#update
		#append
		valores.enter().append("text")
			.attr("x", (d) -> _x(d.data)+5)
			.attr("y", (d,i) -> height-_y2(i+0.8))
			.attr("font-size", "11px")
			.attr("text-anchor", "start")
			.text((d) -> if d.data == -1 then "" else d.data + " " + metricUnit)
		#update + append
		#remove

	width = 640
	height = 480
	
	margins =
		left: 150
		right: 50
		top: 50
		bottom: 50
		
	title = "Grafico"
	labelAxisX = "Eje X"
		
	domainX = []
	metricUnit = ""
		
	colors = []
	semanticColors = []
		
	titleLegend = "Leyenda"
	widthLegend = 200
	
	idTagInsert = "chart"
	
	result.idTagInsert = (value) ->
		if !arguments.length
			idTagInsert
		else
			idTagInsert = value
			result

	result.width = (value) ->
		if !arguments.length
			width
		else
			width = value
			result
			
	result.height = (value) ->
		if !arguments.length
			height
		else
			height = value
			result
	
	result.margins = (value) ->
		if (!arguments.length)
			margins
		else
			margins = value
			result

	result.title = (value) ->
		if (!arguments.length)
			title
		else
			title = value	
			result
			
	result.labelAxisX = (value) ->
		if (!arguments.length)
			labelAxisX
		else
			labelAxisX = value	
			result
 
	result.titleLegend = (value) ->
		if (!arguments.length)
			titleLegend
		else
			titleLegend = value	
			result

	result.widthLegend = (value) ->
		if (!arguments.length)
			widthLegend
		else
			widthLegend = value	
			result
		
	result.domainX = (value) ->
		if (!arguments.length)
			domainX
		else
			domainX = value	
			result
			
	result.metricUnit = (value) ->
		if (!arguments.length)
			metricUnit
		else
			metricUnit = value
			result
			
	result.colors = (value) ->
		if (!arguments.length)
			colors
		else
			colors = value	
			result

	result.semanticColors = (value) ->
		if (!arguments.length)
			semanticColors
		else
			semanticColors = value	
			result

	_svg = () ->
	_x = () ->
	_svgLegend = () ->
	_svgData = () ->
	_line = () -> 
	
	result.build = () ->
		_x  = d3.scale.linear().domain([domainX[0], domainX[domainX.length-1]]).range [0, width]
		
		_line = d3.svg.line()
			.interpolate("linear")
			.x((d,i) -> _x(domainX[i]))

		_svg = d3.select("div#"+idTagInsert)
			.attr("style", "display:block")
			.append("svg")
			.attr("id", idTagInsert)
			.attr("width", width+margins.left+margins.right+widthLegend)
			.attr("height", height+margins.top+margins.bottom)
		
		ticksCountX = domainX.length
		relacionTicksX = Math.round(width / ticksCountX)
		ticksSugeridosX = Math.round(width / 30)
		ticksCountX = ticksSugeridosX if relacionTicksX < 30
	
		xAxis = d3.svg.axis()
			.scale(_x)
			.tickValues(_x.ticks(ticksCountX))
			.orient("bottom")
	
		_svg.append("g")
			.attr("transform", "translate("+margins.left+", "+(height+margins.top)+")")
			.attr("class", "axis")
		  	.call(xAxis)
		  	.select("text")
		  		.attr("x", width/2)
		  		.attr("text-anchor", "middle")
		  		.attr("dy", "25px")
		  		.text(labelAxisX)

		_svg.append("g")
			.attr("transform", "translate("+margins.left+", "+(height+margins.top)+")")
			.attr("class", "axis")
			.append("line")
				.attr("x1", 0)
				.attr("y1", 0)
				.attr("x2", 0)
				.attr("y2", -height)
		  		
		_svg.append("g")
			.attr("transform", "translate("+(margins.left+width/3)+", 15)")
			.attr("class", "title")
			.append("text").text(title)

		_fondoV = _svg.append("g")
			.attr("transform", "translate("+margins.left+", "+margins.top+")")
			.attr("class", "fondo")
		
		_fondoV.selectAll("line")
			.data(domainX)
			.enter().append("line")
				.attr("x1", _x)
				.attr("y1", 0)
				.attr("x2", _x)
				.attr("y2", height)

		#leyenda
		_svgLegend = _svg.append("g")
			.attr("transform", "translate("+(margins.left+width+25)+", "+margins.top+")")
		_svgLegend.append("text").text(titleLegend)
		_svgLegend = _svg.append("g")
			.attr("id", "leyenda")
			.attr("transform", "translate("+(margins.left+width+25)+", "+margins.top+")")

		svgColors = _svgLegend.selectAll("rect")
			.data(semanticColors)
		svgColors
		#svgColors.transition()
		#	.delay((d,i) -> i*100)
		#	.duration(500)
			.attr("y", (d, i) -> 10+(20+5)*i)
		svgColors.enter().append("rect")
			.attr("x", "10")
			.attr("y", (d, i) -> 10+(20+5)*i)
			.attr("width", "20")
			.attr("height", "20")
			.attr("stroke", "#000")
			.attr("stroke-width", "1")
			#.attr("stroke-opacity", "0")
			.attr("fill", (d,i) -> colors[i])
			#.attr("fill-opacity", "0")
			#.transition()
			#	.delay((d,i) -> i*100)
			#	.duration(1500)
				.attr("stroke-opacity", "1")
				.attr("fill-opacity", "1")
		svgColors.exit()
			#.transition()
			#	.duration(500)
			#	.attr("stroke-opacity", "0")
			#	.attr("fill-opacity", "0")
				.remove()

		svgTexts = _svgLegend.selectAll("text")
			.data(semanticColors)
		svgTexts
		#svgTexts.transition()
		#	.delay((d,i) -> i*100)
		#	.duration(500)
			.attr("x", 10+20+5)
			.attr("y", (d,i) -> 10+(20+5)*i)
		svgTexts.enter().append("text")
			.attr("x", 10+20+5)
			.attr("y", (d,i) -> 10+(20+5)*i)
			.attr("dy", "13px")
			#.attr("stroke-opacity", "0")
			#.attr("fill-opacity", "0")
			.text((d,i) -> semanticColors[i])
			#.transition()
			#	.delay((d,i) -> i*100)
			#	.duration(1500)
				.attr("stroke-opacity", "1")
				.attr("fill-opacity", "1")
		svgTexts.exit()
			#.transition()
			#	.duration(500)
			#	.attr("stroke-opacity", "0")
			#	.attr("fill-opacity", "0")
				.remove()

		_svgData = _svg.append("g")
			.attr("id", "series")
			.attr("transform", "translate("+margins.left+", "+margins.top+")")
			
		result
		
	result
