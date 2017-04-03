<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <link rel="stylesheet" type="text/css" href="/resources/styles/css/style.css" />
</head>

<body>
    <div class="container">
        <header>
            <h1>Logout</h1>
        </header>
        <form class="form-horizontal" action="/logout">
            <fieldset>
                <div class="control-group">
                    <div class="controls text">Are you sure you want to logout?</div>
                </div>
                <div class="form-actions">
                    <button id="logoutButton" class="btn btn-danger" type="submit">Logout</button>
                    <input class="btn" type="button" value="Back" onClick="history.back()">
                </div>
            </fieldset>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </form>
    </div>
</body>

</html>
