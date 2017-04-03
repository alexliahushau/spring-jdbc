<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    
    <link rel="stylesheet" href="/resources/styles/css/main.css" />
       <link rel="stylesheet" href="/resources/styles/css/jquery.multiselect.css" />
       <link rel="stylesheet" href="/resources/styles/css/jquery-ui.css" />
       <link rel="stylesheet" href="/resources/styles/css/bootstrap.min.css" />
       
       <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1/jquery-ui.min.js"></script>
       <script src="/resources/js/jquery.multiselect.js"></script>
    <script src="/resources/js/bootstrap.min.js"></script>
    <script src="/resources/js/users.js"></script>

</head>
<body>
    <div class="container">
        <header>
            <h1>
                Users Details
                <div class="logout">
                    <span id="currentUserLogin"> ${principal.name} </span> <a href="/logoutPage"> <i class="icon-off"></i>
                    </a>
                </div>
            </h1>
        </header>
        <div class="courses-top-control">
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th class="span1">Id</th>
                        <th class="span3">first name</th>
                        <th class="span1">last name</th>
                        <th class="span1">birthday</th>
                        <th class="span1">roles</th>
                        <th class="span1">email</th>
                        <th class="span2">tickets</th>
                        <th class="span2">system messages</th>
                    </tr>
                </thead>
                <tbody>
                    <#list users as user>
                    <tr style="display: table-row;">
                        <td>${user.id}</td>
                        <td>${user.firstName?if_exists}</td>
                        <td>${user.lastName?if_exists}</td>
                        <td>${user.birthday?if_exists}</td>
                        <td><#list user.roles?if_exists as role>${role} </#list></td>
                        <td>${user.email?if_exists}</td>
                        <td>${user.tickets?size?if_exists}</td>
                        <td>${user.systemMessages?size?if_exists}</td>
                    </tr>
                    </#list>
                </tbody>
            </table>

            <div class="form-actions">
                <a class="btn" href="/booking">Booking service</a>
            </div>   
        </div>
    </div>
</body>
</html>