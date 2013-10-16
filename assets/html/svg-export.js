// Autor: Daniel Fuentes
//
// Exportador de SVG que lo emebebe todo (tags svg + estilo css) en un string con formato SVG 1.1 standard
// Esta basado en la herramienta svg-crowbar.js
//
// Para mas informacion referirse a los siguientes links:
//   http://nytimes.github.io/svg-crowbar/
//   http://nytimes.github.io/svg-crowbar/svg-crowbar.js
// 
// Requiere la libreria d3 cargada

function exportAllSVG(svgDelimiter) {
	var doctype = '<?xml version="1.0" standalone="no"?><!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">';

	function allSvgToString() {
		var documents = [ window.document ], SVGSources = [];
		d3.selectAll("iframe").each(function() {
			if (this.contentDocument) {
				documents.push(this.contentDocument);
			}
		});
		documents.forEach(function(doc) {
			var styles = getStyles(doc);
			var newSources = getSources(doc, styles);
			// because of prototype on NYT pages
			for ( var i = 0; i < newSources.length; i++) {
				SVGSources.push(newSources[i]);
			}
			;
		})
		result = ""
		if (SVGSources.length > 0) {
			for (p=0; p<SVGSources.length; p++) {
				if (p == 0)
					result = SVGSources[p].source;
				else
					result += svgDelimiter+SVGSources[p].source;
			}
		}
		return result;
	}

	function getSources(doc, styles) {
		var svgInfo = [], svgs = d3.select(doc).selectAll("svg");

		styles = (styles === undefined) ? "" : styles;

		svgs.each(function() {
			var svg = d3.select(this);
			svg.attr("version", "1.1").insert("defs", ":first-child").attr(
					"class", "svg-crowbar").append("style").attr("type",
					"text/css");

			// removing attributes so they aren't doubled up
			svg.node().removeAttribute("xmlns");
			svg.node().removeAttribute("xlink");

			// These are needed for the svg
			if (!svg.node().hasAttributeNS(d3.ns.prefix.xmlns, "xmlns")) {
				svg.node().setAttributeNS(d3.ns.prefix.xmlns, "xmlns",
						d3.ns.prefix.svg);
			}

			if (!svg.node().hasAttributeNS(d3.ns.prefix.xmlns, "xmlns:xlink")) {
				svg.node().setAttributeNS(d3.ns.prefix.xmlns, "xmlns:xlink",
						d3.ns.prefix.xlink);
			}

			var source = (new XMLSerializer()).serializeToString(svg.node())
					.replace('</style>', '<![CDATA[' + styles + ']]></style>');
			var rect = svg.node().getBoundingClientRect();
			svgInfo.push({
				top : rect.top,
				left : rect.left,
				width : rect.width,
				height : rect.height,
				class : svg.attr("class"),
				id : svg.attr("id"),
				childElementCount : svg.node().childElementCount,
				source : [ doctype + source ]
			});
		});
		return svgInfo;
	}

	function getStyles(doc) {
		var styles = "", styleSheets = doc.styleSheets;

		if (styleSheets) {
			for ( var i = 0; i < styleSheets.length; i++) {
				processStyleSheet(styleSheets[i]);
			}
		}

		function processStyleSheet(ss) {
			if (ss.cssRules) {
				for ( var i = 0; i < ss.cssRules.length; i++) {
					var rule = ss.cssRules[i];
					if (rule.type === 3) {
						// Import Rule
						processStyleSheet(rule.styleSheet);
					} else {
						// hack for illustrator crashing on descendent selectors
						if (rule.selectorText) {
							if (rule.selectorText.indexOf(">") === -1) {
								styles += "\n" + rule.cssText;
							}
						}
					}
				}
			}
		}
		return styles;
	}

	return allSvgToString();
}