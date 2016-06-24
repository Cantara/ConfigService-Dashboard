<!DOCTYPE html>
<html ng-app="UseradminApp">
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <link rel="icon" type="image/png" href="img/favicon.ico" />
        <title>JAU Configuration Dash board</title>
        <!--<link href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css" rel="stylesheet">-->
        <link href="css/bootstrap-3.1.1.min.css" rel="stylesheet">
        <link href="css/main.css" rel="stylesheet">
        <link href="css/autocomplete.css" rel="stylesheet">
    </head>
    <body ng-controller="MainCtrl">

        <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#"><img src="img/cantara.png" alt="Cantara" style="max-height: 90%"/>CS Dash board</a>
                </div>
                <div class="collapse navbar-collapse">
                    <ul class="nav navbar-nav">
                        <li ng-class="{active: session.activeTab == 'clients'}"><a href="#/clients">Clients</a></li>
                        <li ng-class="{active: session.activeTab == 'apps'}"><a href="#/apps">Applications</a></li>
                        <li ng-class="{active: session.activeTab == 'about'}"><a href="#/about">About CS Dash board</a></li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <li><a id="logout" href="logout">Log out <strong>${userName}</strong></a></li>
                    </ul>
                </div><!--/.nav-collapse -->
            </div>
        </div>

    </body>
</html>