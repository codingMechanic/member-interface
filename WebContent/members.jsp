<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="de">
    <head>
        <meta charset="UTF-8" />
        <title>LSVN Mitgliederverzeichnis</title>
        <link rel="stylesheet" href="css/bootstrap.min.css" />
        <link rel="stylesheet" href="css/jquery-ui-1.10.3.custom.css" />
        <link rel="stylesheet" href="css/styles.css" />
    </head>
    <body>
        <div id="members">
            <div class="clearfix header">
                <div id="filter"></div>
                <button id="eMailDispatch" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary">
                    <svg version="1.1" class="img-frame" viewBox="0 0 20 10" class="dropdown-svg" >
                        <polygon points="0,0 10,10 20,0 " fill="#1C94C4"/>
                    </svg>
                    eMail-Verteiler
                </button>
                <form action="msScheduler.jsp" method="post">
                    <button name="msScheduler-link" value="upvote" id="msScheduler-link-button" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary hidden">
                        MS-Planer
                    </button>
                </form>
                <button id="newsletterDispatch" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary">
                    <svg version="1.1" class="img-frame" viewBox="0 0 20 10" class="dropdown-svg" >
                        <polygon points="0,0 10,10 20,0 " fill="#1C94C4"/>
                    </svg>
                    Newsletter-Versand
                </button>
                <div id="user-container">
                    <div class="user"></div>
                    <div class="user-body">
                        <p id="openNewPwdDialog">Passwort ändern</p>
                        <p id="logOff"><a href="LogOff">Abmelden</a></p>
                    </div>
                </div>
            </div>
            <div id="emailDispatchCntnr" class="clearfix">
                <button class="dispatch ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary">
                    <div class="img-frame">
                        <svg version="1.1" class="img-frame" width="20px" height="12px" viewBox="0 0 24 14" >
                            <rect x="1" y="1" fill="none" stroke="#1C94C4" stroke-width="2" stroke-miterlimit="10" width="18" height="12"/>
                            <path d="M2,1L18,1L10,9z" fill="none" stroke-width="2" stroke="#1C94C4"/>
                        </svg>
                    </div>eMail senden
                </button>
                <div class="row addressees-row">
                    <div class="col-md-6 addressee-col-1"></div>
                    <div class="col-md-6 addressee-col-2"></div>
                </div>
            </div>
		<div id="newsletterCntnr" class="clearfix">
			<textarea rows="25" cols="150" id="newsletter-input"><h4>Artikelüberschrift hier ...</h4>
<p>Artikeltext hier ...  (bei mehreren Artikeln pro Newsletter diese Struktur entsprechend wiederholen)</p></textarea>
			<p class="newsletter-cntnr-buttons">
				<button
					class="newsletter-test-submit ui-button ui-state-default"
					title="Testen vor dem Abschicken">Newsletter testen</button>
				<button class="newsletter-submit inactive ui-button ui-state-default"
					title="An alle Vereinsmitglieder senden">Newsletter versenden</button>
			</p>
		</div>
		<div class="member-cntnr">
<!--                <div class="row members-row">
                    <div class="col-md-4 member-col-1"></div>
                    <div class="col-md-4 member-col-2"></div>
                    <div class="col-md-4 member-col-3"></div>
                </div>-->
            </div>
        </div>
        <div class="dialog"></div>
        
        <script id="membersRowTemplate" type="text/template">
            <div class="row members-row-{{= rowIndex}}">
                <div class="col-md-4 member-col-1"></div>
                <div class="col-md-4 member-col-2"></div>
                <div class="col-md-4 member-col-3"></div>
            </div>
        </script>
        <script id="memberTemplate" type="text/template">
            <div class="headline clearfix">{{= firstname}} {{= lastname}}<span>{{= membership}}</span></div>
            <div class="col-cntnr clearfix">
                <div class="address clearfix"><div class="str">{{= street}}, </div><div class="plz">{{= plz}} </div><div class="cty">{{= city}}</div></div>
                <div class="left-col">
                    <div class="tel"><span class="label">Tel: </span><span>{{= telephone}}</span></div>
                    <div class="mob"><span class="label">Mobil: </span><span>{{= handy}}</span></div>
                    <div class="mail"><span class="label">Email: </span><span class="addr"><a href="mailto:{{= email}}">{{= email}}</a></span></div>
                    <div class="tel-offc"><span class="label">Tel. dienstl.: </span><span>{{= phoneOffice}}</span></div>
                    <div class="mail-offc"><span class="label">Email dienstl.: </span><span class="addr"><a href="mailto:{{= emailOffice}}">{{= emailOffice}}</a></span></div>
                    <div class="brthd"><span class="label">Geburtstag: </span><span>{{= birthday}}</span></div>
                    <div class="special"><span class="label">Sonderstatus: </span><span>{{= specialStatus}}</span></div>
                </div>
                <div class="right-col">
                    <div class="vote clearfix"><span class="label">Stimmberechtigt </span><input type="checkbox" disabled /></div>
                    <div class="license clearfix"><span class="label">Scheinpilot </span><input type="checkbox" disabled /></div>
                    <div class="youth clearfix"><span class="label">Jugend </span><input type="checkbox" disabled /></div>
                    <div class="ahk clearfix"><span class="label">Kfz mit AHK </span><input type="checkbox" disabled /></div>
                </div>
            </div>
            <div class="med-dates">
                <div class="med-from"><span class="label">Medical seit: </span><span>{{= medFrom}}</span></div>
                <div class="med-to"><span class="label">Medical bis: </span><span>{{= medTo}}</span></div>
            </div>
            <button class="edit ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary">Edit</button>
            <input class="owner" value="{{= owner}}" type="hidden"/>
        </script>
        <script id="memberEditTemplate" type="text/template">
        <form id="member-edit-template" action="#">
            <div class="name-cntnr clearfix">
                <div class="name">
                    <label for="firstname">Vorname</label>
                    <input id="firstname" class="firstname" value="{{= firstname }}" />
                </div>
                <div class="name">
                    <label for="lastname">Nachname</label>
                    <input id="lastname" class="lastname" value="{{= lastname }}" />
                </div>
            </div>
            <input id="membership" type="hidden" value="{{= membership }}" />
            <div class="edit-address clearfix">
            <div>
                <label for="street">Straße</label>
                <input id="street" class="street" value="{{= street }}" />
            </div>
            <div>
                <label for="plz">PLZ</label>
                <input id="plz" class="plz" value="{{= plz }}" />
            </div>
            <div>
                <label for="city">Stadt</label>
                <input id="city" class="city" value="{{= city }}" />
            </div>
            </div>
            <div class="col-cntnr clearfix">
            <div class="left-col">
            <div>
                <label for="telephone">Telefon</label>
                <input id="telephone" class="telephone" value="{{= telephone }}" />
            </div>
            <div>
                <label for="handy">Mobil</label>
                <input id="handy" class="handy" value="{{= handy }}" />
            </div>
            <div>
                <label for="email">eMail</label>
                <input id="email" class="email" value="{{= email }}" />
            </div>
            <div>
                <label for="phoneOffice">Telefon dienstl.</label>
                <input id="phoneOffice" class="phoneOffice" value="{{= phoneOffice }}" />
            </div>
            <div>
                <label for="emailOffice">eMail dienstl.</label>
                <input id="emailOffice" class="emailOffice" value="{{= emailOffice }}" />
            </div>
            <div>
                <label for="birthday">Geburtstag</label>
                <input id="birthday" class="birthday" value="{{= birthday }}" />
            </div>
            <div>
                <label for="specialStatus">Sonderstatus</label>
                <input id="specialStatus" class="specialStatus" value="{{= specialStatus }}" />
            </div>
            </div>
            <div class="right-col">
            <div>
                <label for="voting">stimmberechtigt</label>
                <input type="checkbox" id="voting" class="voting" />
            </div>
            <div>
                <label for="license">Scheinpilot</label>
                <input type="checkbox" id="license" class="license" />
            </div>
            <div>
                <label for="youth">Jugend</label>
                <input type="checkbox" id="youth" class="youth" />
            </div>
            <div>
                <label for="ahk">Kfz mit AHK</label>
                <input type="checkbox" id="ahk" class="ahk" />
            </div>
            </div>
            </div>
            <div class="med-dates">
            <div>
                <label for="medFrom">Medical seit</label>
                <input id="medFrom" class="medFrom" value="{{= medFrom }}" />
            </div>
            <div>
                <label for="medTo">bis</label>
                <input id="medTo" class="medTo" value="{{= medTo }}" />
            </div>
            </div>
            <button class="save ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary">Speichern</button>
            <button class="cancel ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary">Abbrechen</button>
        </form>
        </script>
        <script id="eMailDispatchTemplate" type="text/template">
            <span class="name" title="{{= email}}">{{= firstname}} {{= lastname}}</span><span class="unspecified-note"></span>
            <button class="email-button exclude-email ui-button ui-state-default" title="aus dem Verteiler dieser eMail ausschließen">entfernen</button>
            <button class="email-button include-email ui-button ui-state-default" title="dem Verteiler dieser eMail wieder hinzufügen">hinzu</button>
            <span class="membership">{{= membership}}</span>
        </script>
        <script src="js/libs/jquery-1.11.0.js"></script>
        <script src="js/libs/jquery-ui-1.10.3.custom.js"></script>
        <script src="js/libs/underscore.js"></script>
        <script src="js/libs/backbone-min.js"></script>
        <script src="js/model.js"></script>
        <script src="js/view.js"></script>
        <script src="js/router.js"></script>
        <script src="js/app.js"></script>
        <script src="js/general.js"></script>
    </body>
</html>