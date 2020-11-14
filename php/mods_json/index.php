<?php

$xml = new SimpleXMLElement('<xml/>');
$i = 0;

$files = scandir(".");
natcasesort($files);//Permet de trier sans etre sensible au majuscule


foreach ($files as $file) {
    // Get the contents of the JSON file 
    $strJsonFileContents = file_get_contents($file);
    // Convert to array 
    $array = json_decode($strJsonFileContents, true);
    if($file != "." AND $file != ".." AND $file != "index.php"){
        $track = $xml->addChild(file);//create xml
        $track->addChild('name', "$array[name]");
        $track->addChild('version', "$array[version]");
        $track->addChild('path', "$file");
        $i++;
    }
}
//$track = $xml->addChild('nbFiles',$i);

Header('Content-type: text/xml');
print($xml->asXML());