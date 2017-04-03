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

</head>
<body>
    <div class="container">
        <header>
            <h1>
                User Details
                <div class="logout">
                    <span id="currentUserLogin"> ${principal.name} </span> <a href="/logoutPage"> <i class="icon-off"></i>
                    </a>
                </div>
            </h1>
        </header>
        <div class="courses-top-control">
            <form class="form-horizontal">
                <fieldset>
                    <div class="control-group">
                        <label class="control-label">Id:</label>
                        <div class="controls">
                            <input type="text" value="${dto.user.id}"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">First name:</label>
                        <div class="controls">
                            <input type="text" value="${dto.user.firstName}"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">Last name:</label>
                        <div class="controls">
                            <input type="text" value="${dto.user.lastName}"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">E-mail:</label>
                        <div class="controls">
                            <input type="text" value="${dto.user.email}"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">Birthday:</label>
                        <div class="controls">
                            <input type="text" value="${dto.user.birthday}"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">Account:</label>
                        <div class="controls">
                            <#if dto.userAccount??>
                            <input type="text" value="${dto.userAccount.amount}"/>
                            <#else>
                            <input type="text" value="n/a"/>
                            </#if>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">Tickets:</label>
                        <div class="controls">
                            <select id="tickets" name="tickets" class="span5">
                                <#list dto.user.tickets as ticket>
                                    <option>${ticket.event.name + "  " + ticket.dateTime}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">System messages:</label>
                        <div class="controls">
                            <select id="systemMessages" name="systemMessages" class="span5">
                                <#list dto.user.systemMessages as msg>
                                    <option>${msg}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="form-actions">
                        <a class="btn" href="/user">Back</a>
                    </div>
                </fieldset>
            </form>
        </div>
    </div>
</body>
</html>
