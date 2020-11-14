<?php

$xml = new SimpleXMLElement('<xml/>');

$files = scandir(".");
natcasesort($files);//Permet de trier sans etre sensible au majuscule
$i = 0;

foreach ($files as $file) {
    if($file != "." AND $file != ".." AND $file != "index.php"){ 
        $track = $xml->addChild(file);//create xml
        $track->addChild('path', "$file");
        $i++;
    }
}
//$track = $xml->addChild('nbFiles',$i);
Header('Content-type: text/xml');
print($xml->asXML());