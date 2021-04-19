using UnityEngine;
using UnityEngine.Networking;using System.Collections;
using System.Collections.Generic;
using UnityEngine.Tilemaps;



public class Connection: MonoBehaviour
{

    public GameObject parent;

// game object that gets cloned for every pixel of the wall
    public GameObject wall_cube;

//all tiles - basically colored squares for each pixel of the floor
    public Tile bedroom_tile;
    public Tile bathroom_tile;
    public Tile closet_tile;
    public Tile living_kitchen_dining_tile;
    public Tile balcony_tile;
    public Tile hall_tile;
    public Tile door_window_tile;

// the loading spinner that is seen at the start
    public GameObject spinner;
// the tile map instance whose cells are filled with colored tiles.
    public Tilemap grid;

// details of the floorplan that are populated by the android application
    public int floorplanWidth;
    public int floorplanLength;
    public string floorplanName;
    public string serverIpAddress;
    public string serverPortNumber;
    
    // unity pings to this url to get information - may need to update ip adresses on different device
     public string postURL;




    void Start()
    {
        // IMPORTANT
        // a way of running this function in parallel sort of - only for running on PC, comment otherwise
        //  StartCoroutine(GetText());
    }


    void Update(){
        // for some navigation
        if (Input.GetKeyUp (KeyCode.Escape)) {
            onBackButtonPressed();
        }
    }
    IEnumerator GetText()
    {

        // IMPORTANT - uncomment when running on pc
        // serverIpAddress = "192.168.18.8";
        // serverPortNumber = "5001";
        // "http://192.168.18.8:5001/getFloorplanForUnity"
        postURL = string.Format("http://{0}:{1}/getFloorplanForUnity", serverIpAddress, serverPortNumber);
        // postURL = "http://" + serverIpAddress + ":" + serverPortNumber + "/getFloorplanForUnity";
        // postURL = "http://192.168.18.8:5001/getFloorplanForUnity";
        // initially creating post request to send to server
        WWWForm formData = new WWWForm();
        
        // IMPORTANT
        // these need to be uncommented when testing on PC
        // floorplanName = "cropped2890740263112025085.jpg";
        // floorplanName = "cropped1288408858795941961.jpg";
        formData.AddField("floorplanName", floorplanName);

        Debug.Log(postURL);
        UnityWebRequest www = UnityWebRequest.Post(postURL,formData);


        www.chunkedTransfer = true;
        www.downloadHandler = new DownloadHandlerBuffer();
        yield return www.SendWebRequest();

        if (www.isNetworkError || www.isHttpError)
        {
            Debug.Log(www.error);
        }
        else
        {
            // stop loading bar when data received from server
            spinner.SetActive(false);
            //retrieve results as binary data
            byte[] results = www.downloadHandler.data;


            // IMPORTANT
            // only needed for testing on PC - need to be set according to picture chosen
            // format: floorplanWidth X floorplanLength
			// floorplanWidth = 384;
			// floorplanLength = 437;

            // will hold meaningful data of floorplan array
			int[] odds = new int[floorplanWidth * floorplanLength];
            int count = 0;
            for (int i = 0; i < results.Length; i++)
            {
                // check for various ascii values in array
                if (results[i] == 48 || results[i] == 49 || results[i] == 50 || results[i] == 51 || results[i] == 52 || results[i] == 53 || results[i] == 54 || results[i] == 55 || results[i] == 56)
                {
                    odds[count] = results[i];
                    count++;
                }

            }
            // actual population of floorplan happens here
            // SetTile set colored tiles according to ascii value
            // only wall values are populated with 3d cube gameobjects
            for (int i = 0; i < floorplanLength; i++)
            {
                for (int j = 0; j < floorplanWidth; j++)
                {
                    Vector3Int tile_p=Vector3Int.FloorToInt(new Vector3( i - (floorplanLength/2)+200,j - (floorplanWidth/2)+350 ,5));
                // ascii for 1 corresponds to closet
				  if (odds[floorplanWidth * i + j] == 49)
                    {
                        grid.SetTile(tile_p,closet_tile);
                    }
                    // ascii for 2 corresponds to bathroom
                    else if (odds[floorplanWidth * i + j] == 50)
                    {
                        grid.SetTile(tile_p,bathroom_tile);
                    }
                    // ascii for 3 corresponds to living/kitchen/dining
                    else if (odds[floorplanWidth * i + j] == 51)
                    {
                        grid.SetTile(tile_p,living_kitchen_dining_tile);
                    }
                    // ascii for 4 corresponds to bedroom
                    else if (odds[floorplanWidth * i + j] == 52)
                    {
                        grid.SetTile(tile_p,bedroom_tile);
                    }
                    // ascii for 5 corresponds to hall
                    else if (odds[floorplanWidth * i + j] == 53)
                    {
                        grid.SetTile(tile_p,hall_tile);
                    }
                    // ascii for 6 corresponds to balcony
                    else if (odds[floorplanWidth * i + j] == 54)
                    {
                        grid.SetTile(tile_p,balcony_tile);
                    }
                    // ascii for 7 corresponds to doors/windows
                    else if (odds[floorplanWidth * i + j] == 55)
                    {
                        grid.SetTile(tile_p,door_window_tile);
                    }
                    // ascii for 8 corresponds to wall
                    else if (odds[floorplanWidth * i + j] == 56)
                    {
                        GameObject myObject = Instantiate(wall_cube) as GameObject;
                        myObject.transform.position = new Vector3(i - (floorplanLength/2), 4, j - (floorplanWidth/2) );
                        myObject.transform.SetParent(parent.transform);
                    }
                }
            }
        }
            
    }

    // auxillary methods for android to unity communication
    // only way to send info about floorplan from android to unity.
    // these are actually called by name from android unity activty
    public void setFloorplanName(string name){
        Debug.Log(name);
        floorplanName = name;
    }

    public void setPortNumber(string port){
        Debug.Log(port);
        serverPortNumber = port;
    }
    public void setIpAddress(string ip){
        Debug.Log(ip);
        serverIpAddress = ip;
        // postURL = "http://" + serverIpAddress + ":" + serverPortNumber + "/getFloorplanForUnity";
    }

    public void setFloorplanWidth(string width){
        int temp = int.Parse(width);
        Debug.Log(temp);
        floorplanWidth = temp;
    }

// this is the last function called from android, so at its end we start 3d model construction
    public void setFloorplanLength(string length){
        int temp = int.Parse(length);
        Debug.Log(temp);
        floorplanLength = temp;
        // IMPORTANT
        StartCoroutine(GetText());
    }
    public void onBackButtonPressed(){
        AndroidJavaClass jc = new AndroidJavaClass ("com.unity3d.player.UnityPlayer"); 
                    AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject> ("currentActivity"); 
                    jo.Call ("onBackPressed");
    }
}