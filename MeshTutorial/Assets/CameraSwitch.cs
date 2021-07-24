using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class CameraSwitch : MonoBehaviour
{
    public GameObject house;
    public GameObject imageTarget;
    public GameObject grid;

    public GameObject firstPersonCamera;
    public GameObject overheadCamera;
    public GameObject arCamera;

    public GameObject birdseyeBtn;
    public GameObject topviewBtn;
    public GameObject arViewBtn;
    public GameObject recenterBtn;

    public GameObject colorLegend;
    public bool colorLegendShown = false;




    // Call this function to disable FPS camera,
    // and enable overhead camera.
    public void ShowTopView() {
        Debug.Log("Switching to overhead view!");
        firstPersonCamera.SetActive(false);
        overheadCamera.SetActive(true);

        topviewBtn.SetActive(false);
        birdseyeBtn.SetActive(true);



    }

/*    public void RecenterBirdEyeCamera() {

        firstPersonCamera.transform.position = new Vector3(0.0f, 600.0f, 0.0f);
        firstPersonCamera.transform.Rotate(90,0,0);

    }*/


    public void SwitchToAR()
    {
        Debug.Log("Switching to AR view!");
        firstPersonCamera.SetActive(false);
        //overheadCamera.SetActive(false);

        //grid.SetActive(false);
        grid.transform.SetParent(imageTarget.transform);
        house.transform.SetParent(imageTarget.transform);


        arCamera.SetActive(true);

        //topviewBtn.SetActive(false);
       // birdseyeBtn.SetActive(true);
        //recenterBtn.SetActive(false);

        arViewBtn.SetActive(false);


    }


    // Call this function to enable FPS camera,
    // and disable overhead camera.
    public void ShowBirdsEyeView() {
        Debug.Log("Switching to birdseye view!");
        //overheadCamera.SetActive(false);
        arCamera.SetActive(false);

        grid.SetActive(true);
        house.transform.SetParent(null);
        grid.transform.SetParent(null);

        for (int a = 0; a < house.transform.childCount; a++)
        {
            house.transform.GetChild(a).GetComponent<Renderer>().enabled = true;
        }
        grid.transform.GetChild(0).GetComponent<Renderer>().enabled = true;
        //house.SetActive(true);

        firstPersonCamera.SetActive(true);

        birdseyeBtn.SetActive(false);
        //recenterBtn.SetActive(true);

        arViewBtn.SetActive(true);
        //topviewBtn.SetActive(true);

    }

        public void ToggleColorLegend() {
        Debug.Log("Toggling color legend!");
        if(colorLegendShown){
            colorLegend.SetActive(false);
            colorLegendShown=false;
        }else{
            colorLegend.SetActive(true);
            colorLegendShown=true;
        }

    }
}
