module devtools.components {

  exports de.hhn.it.devtools.components.vactrack;
  requires org.slf4j;
  requires devtools.apis;

  provides de.hhn.it.devtools.apis.vactrack.VacTrackService
          with de.hhn.it.devtools.components.vactrack.VacTrackServiceImpl;

        }
