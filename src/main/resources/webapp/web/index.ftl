<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>ConfigService Dashboard</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="//maxcdn.bootstrapcdn.com/bootswatch/3.2.0/cosmo/bootstrap.min.css" rel="stylesheet" />
    <link href="//cdnjs.cloudflare.com/ajax/libs/animate.css/3.1.0/animate.min.css" rel="stylesheet" />
    <link href="//fonts.googleapis.com/css?family=Roboto+Slab:100,300,400,700" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="css/main.css" />
    <link href="//cdnjs.cloudflare.com/ajax/libs/owl-carousel/1.3.2/owl.carousel.css" rel="stylesheet" type="text/css" />
    <link href="//cdnjs.cloudflare.com/ajax/libs/owl-carousel/1.3.2/owl.theme.min.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" href="https://npmcdn.com/angular-toastr/dist/angular-toastr.css" />
    <!--



    -->
</head>

<body ng-app="app" ng-controller="RootController">
<!--<body ng-app="app" ng-controller="RootController">-->
<div class="navbar navbar-default navbar-fixed-top top-navbar">
    <div class="container-fluid">
        <div class="navbar-header">
            <div class="navbar-brand">ConfigService Dashboard</div>
        </div>
        <ul class="nav navbar-nav navbar-right">
           <li><a id="logout" href="#/login">Log out</a></li>
        </ul>
    </div>
    <div id="top-nav-container" class="second-top-nav">
        <div id="top-nav" ng-include="currentRoute.topNav">
        </div>
    </div>
</div>
<div class="container-fluid">
    <div id="content-container" class="row">

        <!--  OLD LAYOUT
        <div class="col-sm-2 left-nav-bar" ng-if="currentRoute.leftNav">
            <div id="left-nav" ng-include="currentRoute.leftNav">
            </div>
        </div>
        <div class="col-sm-10 col-sm-offset-2">
            <label ng-if="routeHasError" class="alert alert-danger">{{routeError}}</label>
            <div id="page-content" ng-view></div>
        </div>
        -->
         <label ng-if="routeHasError" class="alert alert-danger">{{routeError}}</label>
         <div id="page-content" ng-view></div>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.3/angular.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.3/angular-route.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.3/angular-resource.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.3/angular-sanitize.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.3/angular-animate.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.3/angular-messages.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.10.0/ui-bootstrap-tpls.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/owl-carousel/1.3.2/owl.carousel.js"></script>
<script src="https://npmcdn.com/angular-toastr/dist/angular-toastr.tpls.js"></script>
<script src="https://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.13.0.js"></script>
<script src="https://code.angularjs.org/1.2.13/angular-cookies.js"></script>

<script src="js/vendor/smart-table.debug.js"></script>
<script src="js/vendor/smart-table-directives.js"></script>
<script src="js/vendor/elastic.js"></script>
<script src="js/app.js"></script>
<script src="js/root.js"></script>
<script src="js/shared/directives.js"></script>
<script src="js/Client/controllers.js"></script>
<script src="js/Client/services.js"></script>
<script src="js/Application/controllers.js"></script>
<script src="js/Application/services.js"></script>
<script src="js/Authentication/controllers.js"></script>
<script src="js/Authentication/services.js"></script>
<script src="js/shared/models.js"></script>
<script src="js/shared/services.js"></script>
<script src="js/config.js"></script>


</body>

</html>


