import { useParams } from "react-router-dom";


const KibanaEmbed : React.FC = () => {
    const { serialNumber } = useParams<{ serialNumber: string }>(); 
    const kibanaUrl = `http://localhost:5601/app/dashboards#/view/14b4f5b6-06c9-4e1c-97c7-25f1968d29fa?embed=true&_g=(refreshInterval%3A(pause%3A!t%2Cvalue%3A60000)%2Ctime%3A(from%3Anow-15m%2Cto%3Anow))&hide-filter-bar=true_a=(filters:!(('$state':(store:appState),meta:(alias:!n,disabled:!f,field:sensor_serial_number.keyword,index:d905f108-ae1f-4823-a63e-05d5f5cc4bfb,key:sensor_serial_number.keyword,negate:!f,params:(query:'${serialNumber}'),type:phrase),query:(match_phrase:(sensor_serial_number.keyword:'${serialNumber}')))))`;
    return <iframe 
                src={kibanaUrl}
                style={{ width: "100%", height: "100%", border: "none" }} 
                title="Kibana Dashboard for Sensor ${serialNumber}"
                />
}


export default KibanaEmbed;