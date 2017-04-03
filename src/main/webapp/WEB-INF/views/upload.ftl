<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8" />
    <meta name="_csrf" content="${_csrf.token}" />
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf_header" content="${_csrf.headerName}" />

    <link rel="stylesheet" href="/resources/styles/css/main.css" />
    <link rel="stylesheet" href="/resources/styles/css/jquery.multiselect.css" />
    <link rel="stylesheet" href="/resources/styles/css/jquery-ui.css" />
    <link rel="stylesheet" href="/resources/styles/css/bootstrap.min.css" />

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1/jquery-ui.min.js"></script>
    <script src="/resources/js/jquery.multiselect.js"></script>
    <script src="/resources/js/bootstrap.min.js"></script>

</head>

<body>
    <div class="container">
        <header>
            <h1>
                Uploading Service
                <div class="logout">
                   <span id="currentUserLogin">${principal.name}</span>
                        <a href="/logoutPage">
                            <i class="icon-off"></i>
                        </a>
                </div>
            </h1>
        </header>

        <div class="courses-top-control">
            <form method="POST" enctype="multipart/form-data" action="/upload/users">
                <table>
                    <tr>
                        <td>Choose file to upload users:</td>
                        <td>
                            <input type="file" name="file" />
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <input type="submit" value="Upload" />
                        </td>
                    </tr>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </table>
            </form>

            <form method="POST" enctype="multipart/form-data" action="/upload/events">
                <table>
                    <tr>
                        <td>Choose file to upload events:</td>
                        <td>
                            <input type="file" name="file" />
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <input type="submit" value="Upload" />
                        </td>
                    </tr>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </table>
            </form>
        </div>
        <div class="form-actions">
            <a class="btn" href="/booking">Booking service</a>
        </div>
    </div>
</body>

</html>