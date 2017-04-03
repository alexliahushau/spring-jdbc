<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    
    <link rel="stylesheet" href="/resources/styles/css/main.css" />
    <link rel="stylesheet" href="/resources/styles/css/jquery.multiselect.css" />
    <link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1/themes/ui-lightness/jquery-ui.css" />
    <link rel="stylesheet" href="/resources/styles/css/bootstrap.min.css" />
       
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1/jquery-ui.min.js"></script>
    <script src="/resources/js/jquery.multiselect.js"></script>
    <script src="/resources/js/bootstrap.min.js"></script>
    <script src="/resources/js/booking.js"></script>

</head>
    <body>
        <div class="container">
            <header>
                <h1>
                    Booking Service
                    <div class="logout">
                        <span id="currentUserLogin">${principal.name}</span>
                        <a href="/logoutPage">
                            <i class="icon-off"></i>
                        </a>
                    </div>
                </h1>
            </header>
            <div class="courses-top-control">
                <div id="msgBox"></div>
                <ul class="nav nav-tabs">
                      <li class="active"><a data-toggle="tab" href="#tab1">Get tickets price</a></li>
                      <li><a id="getBookedTickets" data-toggle="tab" href="#tab2">Get My booked tickets</a></li>
                    <#if principal.authorities?seq_contains("ROLE_BOOKING_MANAGER")>
                        <li><a id="getBookedTickets" data-toggle="tab" href="#tab3">Get booked tickets for event</a></li>
                    </#if>
                </ul>
                
                <div class="tab-content">
                      <div id="tab1" class="tab-pane fade in active">
                        <h3>Get tickets price</h3>
                        <form class="form-horizontal" id="getTicketsPrice" onsubmit="return getTicketsPrice(this);">
                            <fieldset>
                                <div class="control-group">
                                    <label class="control-label">Choose event:</label>
                                    <div class="controls">
                                        <select id="events" name="event" class="span5">
                                        </select>
                                    </div>
                                </div>
                                <div class="control-group">
                                    <label class="control-label">Choose event air dates:</label>
                                    <div class="controls">
                                        <select id="airDates" name="airDate" class="span5">
                                        </select>
                                    </div>
                                </div>
                                <div class="control-group">
                                    <label class="control-label">Choose seats:</label>
                                    <div class="controls">
                                        <select id="seats" name="seats" class="span5">
                                        </select>
                                    </div>
                                </div>
                                <div class="form-actions">
                                    <span id="totalPrice" class="btn btn-primary totalPrice">Total price: $0</span>
                                    <span id="bookTickets" class="btn btn-primary">Book tickets</span>
                                    <a class="btn" href="javascript:void(0)">Cancel</a>
                                    <#if principal.authorities?seq_contains("ROLE_BOOKING_MANAGER")>
                                        <a class="btn" href="/user">Watch all users</a>
                                    </#if>
                                    <#if principal.authorities?seq_contains("ROLE_ADMIN")>
                                        <a class="btn" href="/upload">Uploading service</a>
                                        <a class="btn" href="/user">Watch all users</a>
                                    </#if>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                      
                    <div id="tab2" class="tab-pane fade">
                        <h3>My booked tickets</h3>
                        <table id="tickets" class="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th class="span1">Id</th>
                                <th class="span3">Event name</th>
                                <th class="span3">Auditorium</th>
                                <th class="span3">Date</th>
                                <th class="span1">Seat</th>
                                <th class="span1">Rating</th>
                                <th class="span2">Purchased</th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                        </table>
                        <a id="pdfView" class="btn btn-success" href="/booking/tickets/">PDF VIEW</a>
                        <a id="buyTickets" class="btn btn-success">Buy tickets</a>
                    </div>
                    
                    <div id="tab3" class="tab-pane fade">
                        <h3>Get booked tickets for event</h3>
                        <table id="events" class="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th class="span1">Id</th>
                                <th class="span3">Event name</th>
                                <th class="span3">Air dates</th>
                                <th class="span3">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                        </table>
                    </div>
                    
                </div>
            </div>
        </div>
    </body>
</html>
