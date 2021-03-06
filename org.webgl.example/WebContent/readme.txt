1. Online links:
	http://learningwebgl.com/blog/?p=28
	http://learningwebgl.com/blog/?page_id=1217
	
	https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API
	https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API/Tutorial
	
	http://webglfundamentals.org/ (WebGL Fundamentals)
	
	http://threejs.org/examples/
	
	https://www.khronos.org/webgl/
	http://stackoverflow.com/questions/tagged/webgl
	https://groups.google.com/forum/#!forum/webgl-dev-list


2. Sessions:
http://learningwebgl.com/blog/?p=28
http://learningwebgl.com/blog/?page_id=1217
https://github.com/gpjt/webgl-lessons

	Test URLs:
	http://localhost:8080/gl/
	http://localhost:8080/gl/lesson01/index.html


3. WebGL API:
https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API

WebGL tutorial:
https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API/Tutorial
http://localhost:8080/gl/tutorial.html

    WebGL tutorial 1 - Getting started with WebGL
        https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API/Tutorial/Getting_started_with_WebGL

		Test URL:
		http://localhost:8080/gl/tutorial1.html

    WebGL tutorial 2 - Adding 2D content to a WebGL context
        https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API/Tutorial/Adding_2D_content_to_a_WebGL_context
	    https://github.com/mdn/webgl-examples/tree/gh-pages/tutorial/sample2
	    http://mdn.github.io/webgl-examples/tutorial/sample2/

        Note:
        The OpenGL® ES Shading Language (https://www.khronos.org/registry/gles/specs/2.0/GLSL_ES_Specification_1.0.17.pdf)

		Test URL:
		http://localhost:8080/gl/tutorial2.html
		http://localhost:8080/gl/tutorial2v2.html

	WebGL tutorial 3 - Using shaders to apply color in WebGL
		https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API/Tutorial/Using_shaders_to_apply_color_in_WebGL
		https://github.com/mdn/webgl-examples/tree/gh-pages/tutorial/sample3
		http://mdn.github.io/webgl-examples/tutorial/sample3/

		Test URL:
		http://localhost:8080/gl/tutorial3.html
		http://localhost:8080/gl/tutorial3v2.html
	
	WebGL tutorial 4 - Animating objects with WebGL
		https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API/Tutorial/Animating_objects_with_WebGL
		https://github.com/mdn/webgl-examples/tree/gh-pages/tutorial/sample4
		http://mdn.github.io/webgl-examples/tutorial/sample4/

		Test URL:
		http://localhost:8080/gl/tutorial4.html

	WebGL tutorial 5 - Creating 3D objects using WebGL
		https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API/Tutorial/Creating_3D_objects_using_WebGL
		https://github.com/mdn/webgl-examples/tree/gh-pages/tutorial/sample5
		http://mdn.github.io/webgl-examples/tutorial/sample5/
	
		Test URL:
		http://localhost:8080/gl/tutorial5.html

	WebGL tutorial 6 - Using textures in WebGL
		https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API/Tutorial/Using_textures_in_WebGL
		https://github.com/mdn/webgl-examples/tree/gh-pages/tutorial/sample6
		http://mdn.github.io/webgl-examples/tutorial/sample6/

		Test URL:
		http://localhost:8080/gl/tutorial6.html

		Non power-of-two textures
		Generally speaking, using textures whose sides are a power of two is ideal. They are efficiently stored in video memory and are not restricted in how they could be used. 
		Artist-created textures should be scaled up or down to a nearby power of two and, really, should have been authored in power-of-two to begin with. 
		Each side should be: 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, or 2048 pixels. Many, but not all, devices can support 4096 pixels; some can support 8192 and above.

	WebGL tutorial 7 - Lighting in WebGL
		https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API/Tutorial/Lighting_in_WebGL
		https://github.com/mdn/webgl-examples/tree/gh-pages/tutorial/sample7
		http://mdn.github.io/webgl-examples/tutorial/sample7/

		Note:
		Phong shading
		https://en.wikipedia.org/wiki/Phong_shading
		ambient reflection		// environmental light
		diffuse reflection		// sun light
		specular reflection		// bulb light

		Test URL:
		http://localhost:8080/gl/tutorial7.html

	
	Read:
	What is a transformation matrix?
	https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API/Matrix_math_for_the_web
	
	WebGL model view projection
	https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API/WebGL_model_view_projection


4. WebGL Fundamentals
http://webglfundamentals.org/

	Example1 - http://webglfundamentals.org/webgl/lessons/webgl-fundamentals.html
		http://webglfundamentals.org/webgl/webgl-fundamentals.html
		http://webglfundamentals.org/webgl/webgl-2d-rectangle.html
		http://webglfundamentals.org/webgl/webgl-2d-rectangle-top-left.html

		Read:
		WebGL How It Works - 			http://webglfundamentals.org/webgl/lessons/webgl-how-it-works.html
		WebGL Shaders and GLSL - 		http://webglfundamentals.org/webgl/lessons/webgl-shaders-and-glsl.html
		WebGL Boilerplate - 			http://webglfundamentals.org/webgl/lessons/webgl-boilerplate.html
		A Tiny WebGL helper Library - 	http://twgljs.org/

		Test URL:
		http://localhost:8080/gl/fundamentals/example1_v1.html
		http://localhost:8080/gl/fundamentals/example1_v2.html
		http://localhost:8080/gl/fundamentals/example1_v3.html
		http://localhost:8080/gl/fundamentals/example1_v4.html


5. WebGL 101
Q1. what is vec4? 
	e.g. gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0)

	Answer: 
	It's a vec4 (4 component vector). X is the red component, Y the green, and Z the blue. The last component (w) is the alpha.
	(from http://stackoverflow.com/questions/35926331/webgl-computer-graphics-vec4)


