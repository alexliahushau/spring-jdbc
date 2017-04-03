<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	
	<link rel="stylesheet" href="/resources/styles/css/main.css" />
   	<link rel="stylesheet" href="/resources/styles/css/bootstrap.min.css" />

    <script src="/resources/js/bootstrap.min.js"></script>


</head>
<body>
    <div class="container">
        <header>
            <h1>
                Error page
                <div class="logout">
                    <span id="currentUserLogin"> anonim </span> <a href="/logoutPage"> <i class="icon-off"></i>
                    </a>
                </div>
            </h1>
        </header>
        
        <div class="courses-top-control">
            <p>Failed URL: ${url?if_exists}</p>
    		<p>Exception:  ${exception.message?if_exists}</p>
     		<#list exception.stackTrace as msg>
     		    <p>${msg?if_exists}</p> 
    		</#list>
        </div>
    </div>
</body>
</html>