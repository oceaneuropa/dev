    //A global variable for the WebGL context <WebGLRenderingContext> 
	// var canvas;
	var gl;
	var squareVerticesBuffer;
	var squareVerticesColorBuffer;
	var mvMatrix;
	var shaderProgram;
	var vertexPositionAttribute;
	var vertexColorAttribute;
	var perspectiveMatrix;
	// var horizAspect = 480.0/640.0;

    /**
     * set up the WebGL context and start rendering content. 
     */
	function start() {
		console.log("start()");

		canvas = document.getElementById("glcanvas");

		// Initialize the GL context
		gl = initWebGL(canvas);

		// Only continue if WebGL is available and working
		if (gl) {
			// Set clear color to black, fully opaque
			// Note:
		    // 1. looks like this method is to set "background" color
			// 2. (r, g, b, opaque) each value ranges from 0 to 1
			gl.clearColor(0.0, 0.0, 0.0, 1.0);

			// Enable depth testing
			gl.enable(gl.DEPTH_TEST);

			// Near things obscure far things
			gl.depthFunc(gl.LEQUAL);

			// Clear the color as well as the depth buffer.
			gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

		    // Initialize the shaders where all the lighting for the vertices and so forth is established.
		    initShaders();

		    // Call the routine that builds buffers where all the objects will be drawing with.
		    initBuffers();

		    // Set up to draw the scene periodically.
		    setInterval(drawScene, 150);
		}
	}

    /**
     *  initialize the WebGL context.
     */
	function initWebGL(canvas) {
		console.log("initWebGL()");

		gl = null;
		try {
			// Try to grab the standard context. If it fails, fallback to experimental.
			gl = canvas.getContext("webgl") || canvas.getContext("experimental-webgl");
		} catch (e) {
		}
		// If we don't have a GL context, give up now
		if (!gl) {
			alert("Unable to initialize WebGL. Your browser may not support it.");
			gl = null;
		}

		// Modify the rendered resolution of a WebGL context with variables gl and canvas as used in the above example:
		// gl.viewport(0, 0, canvas.width, canvas.height);

		return gl;
	}

	function initShaders() {
		console.log("initShaders()");

		// get shader obj <WebGLShader> for MIME type "x-shader/x-fragment"
		var fragmentShader = getShader(gl, "shader-fs");
		// get shader obj <WebGLShader> for MIME type "x-shader/x-vertex"
		var vertexShader = getShader(gl, "shader-vs");

		// Create the shader program <WebGLProgram>
		shaderProgram = gl.createProgram();

		// Attach pre-existing shaders
		gl.attachShader(shaderProgram, vertexShader);
		gl.attachShader(shaderProgram, fragmentShader);

		gl.linkProgram(shaderProgram);

		// If creating the shader program failed, alert
		if (!gl.getProgramParameter(shaderProgram, gl.LINK_STATUS)) {
			alert("Unable to initialize the shader program.");
		}

		gl.useProgram(shaderProgram);

		// Note:
		// it seems that vertexPositionAttribute and vertexColorAttribute are pointers into GL.

		// Reasons:
		// (1) initBuffers() is not called yet at this point. 
		// the squareVerticesBuffer and squareVerticesColorBuffer are initialized with the vertices array and the colors array in that function.
		// so the squareVerticesBuffer and squareVerticesColorBuffer are not initialized now

		// (2) but "shader-fs" and "shader-vs" have been compiled by now, since this function calls getShader(), where shader gets compiled.
		// and the only other place that "aVertexPosition" and "aVertexColor" are used are in the "shader-fs" and "shader-vs" script.
		// the script there actually gets value from "aVertexPosition" attribute and "aVertexColor" attribute.
		// so vertexPositionAttribute and vertexColorAttribute should have no value now.

		// (3) 
		// squareVerticesBuffer is set to "aVertexPosition" in drawScene()
		// squareVerticesColorBuffer is set to "aVertexColor" in drawScene()
		// until then vertexPositionAttribute and vertexColorAttribute should have no values. 

		// 1. initialize the position attribute for the shaderProgram
		vertexPositionAttribute = gl.getAttribLocation(shaderProgram, "aVertexPosition");
		gl.enableVertexAttribArray(vertexPositionAttribute);

		// 2. initialize the color attribute for the shaderProgram
		vertexColorAttribute = gl.getAttribLocation(shaderProgram, "aVertexColor");
		gl.enableVertexAttribArray(vertexColorAttribute);
	}

	/**
	 * Fetches a shader program with the specified name from the DOM, returning the compiled shader program to the caller, 
	 * or null if it couldn't be loaded or compiled.
	 */
	function getShader(gl, id) {
		// find shaderScript <?> object by id
		var shaderScript = document.getElementById(id);
		if (!shaderScript) {
			return null;
		}

		// Once the element with the specified ID is found, its text is read into the variable theSource.
		// Note:
		// shaderScript obj contains children nodes, for each Text child node, append its text content to theSource
		var theSource = "";
		var currentChild = shaderScript.firstChild;
		while (currentChild) {
			if (currentChild.nodeType == currentChild.TEXT_NODE) {
				theSource += currentChild.textContent;
			}
			currentChild = currentChild.nextSibling;
		}

		// Once the code for the shader has been read, we take a look at the MIME type of the shader object to determine whether 
		// it's a vertex shader (MIME type "x-shader/x-vertex") or a fragment shader (MIME type "x-shader/x-fragment"), 
		// then create the appropriate type of shader from the retrieved source code.

		// shader object <WebGLShader> to be returned
		var shader;
		if (shaderScript.type == "x-shader/x-fragment") {
			shader = gl.createShader(gl.FRAGMENT_SHADER);
		} else if (shaderScript.type == "x-shader/x-vertex") {
			shader = gl.createShader(gl.VERTEX_SHADER);
		} else {
			// Unknown shader type
			return null;
		}

		// the source is passed into the shader and compiled. If an error occurs while compiling the shader, 
		// we display an alert and return null; otherwise, the newly compiled shader is returned to the caller.
		gl.shaderSource(shader, theSource);

		// Compile the shader program
		gl.compileShader(shader);

		// See if it compiled successfully
		if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
			alert("An error occurred compiling the shaders: " + gl.getShaderInfoLog(shader));
			return null;
		}
		return shader;
	}

	/**
	 * point2 ---------- point1
	 *   |					|
	 *   |					|
	 *   |					|
	 *   |					|
	 *   |					|
	 *   |					|
	 * point4 ---------- point3
	 *
	 * create the buffer that contains its vertices
	 */
	function initBuffers() {
		console.log("initBuffers()");

		// 1. create squareVerticesBuffer with vertices array
		// JavaScript array containing the coordinates for each vertex of the square
		var vertices = [ 
			1.0,  1.0,  0.0, // point1
		   -1.0,  1.0,  0.0, // point2 
			1.0, -1.0,  0.0, // point3
		   -1.0, -1.0,  0.0  // point4
		];
		// obtain a buffer into which the vertices will be stored.
		squareVerticesBuffer = gl.createBuffer();
		// the buffer is bound to the WebGL context
		gl.bindBuffer(gl.ARRAY_BUFFER, squareVerticesBuffer);
		// the array is converted into an array of WebGL floats and passed into the 
		// gl object's bufferData() method to establish the vertices for the object.
		gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);

		// 2. create squareVerticesColorBuffer with colors array
		// JavaScript array containing four 4-value vectors, one for each vertex's color. 
		// Then a new WebGL buffer is allocated to store these colors, and 
		var colors = [
			1.0,  1.0,  1.0,  1.0,	// point1 white
		  	1.0,  0.0,  0.0,  1.0,	// point2 red
		  	0.0,  1.0,  0.0,  1.0,	// point3 green
		  	0.0,  0.0,  1.0,  1.0	// point4 blue
		];
		// obtain a buffer into which the colors will be stored
		squareVerticesColorBuffer = gl.createBuffer();
		// the buffer is bound to the WebGL context
		gl.bindBuffer(gl.ARRAY_BUFFER, squareVerticesColorBuffer);
		// the array is converted into WebGL floats and stored into the buffer.
		gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(colors), gl.STATIC_DRAW);
	}

	function drawScene() {
		console.log("drawScene()");

		// clear the context to our background color
		gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

		// establish the camera's perspective. 
		// set a field of view of 45°, with a width to height ratio of 640/480 (the dimensions of our canvas). 
		// specify that we only want objects between 0.1 and 100 units from the camera to be rendered.
		perspectiveMatrix = makePerspective(45, 640.0/480.0, 0.1, 100.0);

		// establish the position of the square by loading the identity position and translating away from the camera by 6 units. 
		loadIdentity();
		mvTranslate([ -0.0, 0.0, -6.0 ]); // -6 means move z into screen by 6 units

		// 1. bind the square's vertex buffer to the context, configure it
		gl.bindBuffer(gl.ARRAY_BUFFER, squareVerticesBuffer);
		gl.vertexAttribPointer(vertexPositionAttribute, 3, gl.FLOAT, false, 0, 0);

		// 2. use these colors when drawing the square:
		gl.bindBuffer(gl.ARRAY_BUFFER, squareVerticesColorBuffer);
		gl.vertexAttribPointer(vertexColorAttribute, 4, gl.FLOAT, false, 0, 0);

		setMatrixUniforms();

		// draw the object by calling the drawArrays() method.
		gl.drawArrays(gl.TRIANGLE_STRIP, 0, 4);
	}

	// ------------------------------------------------------------------------
	// Matrix utility functions
	// ------------------------------------------------------------------------
	function loadIdentity() {
	  	mvMatrix = Matrix.I(4);
	}

	function multMatrix(m) {
	  	mvMatrix = mvMatrix.x(m);
	}

	function mvTranslate(v) {
	  	multMatrix(Matrix.Translation($V([v[0], v[1], v[2]])).ensure4x4());
	}

	function setMatrixUniforms() {
		var pUniform = gl.getUniformLocation(shaderProgram, "uPMatrix");
		gl.uniformMatrix4fv(pUniform, false, new Float32Array(perspectiveMatrix.flatten()));

		var mvUniform = gl.getUniformLocation(shaderProgram, "uMVMatrix");
		gl.uniformMatrix4fv(mvUniform, false, new Float32Array(mvMatrix.flatten()));
	}
