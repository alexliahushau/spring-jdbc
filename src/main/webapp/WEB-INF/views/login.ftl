<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8" />
    <link rel="stylesheet" type="text/css" href="/resources/styles/css/style.css" />
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
</head>

<body>
    <div class="container">
        <header>
            <h1>Login</h1>
        </header>
        <div id="err" class="alert-error">
        </div>

        <form name='loginForm' class="form-horizontal" action="/login" method="POST">
            <fieldset>
                <div class="control-group">
                    <label class="control-label">Login</label>
                    <div class="controls">
                        <div class="input-prepend">
                            <span class="add-on">@</span>
                            <input id="username" name="username" class="span3" type="text" />
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">Password</label>
                    <div class="controls">
                        <input id="password" name="password" class="span3" type="password" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">Remember Me</label>
                    <div class="controls">
                        <input type="checkbox" name="remember-me" />
                    </div>
                </div>
                <div class="form-actions">
                    <button id="loginButton" class="btn btn-primary" type="submit">Login</button>
                </div>
                
            </fieldset>
            <input name="_csrf" type="hidden" value="${_csrf.token}">
        </form>
    </div>
    <script>
        var getURLParameter = function (name) {
                return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null;
            },
            alert = $('#err');

        if (getURLParameter('error')) {

            alert.addClass('alert');
            alert.append($('<span>Dad cridencials</span>'));
        } else {
            alert.removeClass('alert');
        }
    </script>
</body>

</html>