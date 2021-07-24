using System.Collections;
using System.Collections.Generic;
using UnityEngine;
 
public class FirstPersonCameraHandler : MonoBehaviour {
 
    public Transform target;
    public Vector3 targetOffset;
    public float distance = 500f;
    public float maxDistance = 500;
    public float minDistance = .6f;
    public float xSpeed = 5.0f;
    public float ySpeed = 5.0f;
    public int yMinLimit = 0;
    public int yMaxLimit = 90;
    public float zoomRate = 0.025f;
    public float panSpeed = 0.3f;
    public float zoomDampening = 5.0f;
 
    private float xDeg = 0.0f;
    private float yDeg = 0.0f;
    private float currentDistance;
    private float desiredDistance;
    private Quaternion currentRotation;
    private Quaternion desiredRotation;
    private Quaternion rotation;
    private Vector3 position;
 
    private Vector3 FirstPosition;
    private Vector3 SecondPosition;
    private Vector3 delta;
    private Vector3 lastOffset;
    private Vector3 lastOffsettemp;
  


 
 
    void Start() { Init(); }
    void OnEnable() { Init(); }
 
    public void Init()
    {
        //If there is no target, create a temporary target at 'distance' from the cameras current viewpoint
        if (!target)
        {
            GameObject go = new GameObject("Cam Target");
            go.transform.position = transform.position + (transform.forward * distance);
            target = go.transform;
        }
 
        distance = Vector3.Distance(transform.position, target.position);
        currentDistance = distance;
        desiredDistance = distance;
 
        //be sure to grab the current rotations as starting points.
        position = transform.position;
        rotation = transform.rotation;
        currentRotation = transform.rotation;
        desiredRotation = transform.rotation;
 
        xDeg = Vector3.Angle(Vector3.right, transform.right);
        yDeg = Vector3.Angle(Vector3.up, transform.up);

    }
    void LateUpdate()
    {
        //for zooming on pinch
        if (Input.touchCount==2)
        {
            Touch touchZero = Input.GetTouch (0);
 
            Touch touchOne = Input.GetTouch (1);
 
            Vector2 touchZeroPreviousPosition = touchZero.position - touchZero.deltaPosition;
            Vector2 touchOnePreviousPosition = touchOne.position - touchOne.deltaPosition;
 

            float prevTouchDeltaMag = (touchZeroPreviousPosition - touchOnePreviousPosition).magnitude;
            float TouchDeltaMag = (touchZero.position - touchOne.position).magnitude;
  
            float deltaMagDiff = prevTouchDeltaMag - TouchDeltaMag;
             desiredDistance += deltaMagDiff * Time.deltaTime * zoomRate * Mathf.Abs(desiredDistance);
        }
        //single touch movement will cause camera to rotate
        if (Input.touchCount==1 && Input.GetTouch(0).phase == TouchPhase.Moved)
        {
            Vector2 touchposition = Input.GetTouch(0).deltaPosition;
            xDeg += touchposition.x * xSpeed * 0.01f;
            yDeg -= touchposition.y * ySpeed * 0.01f;
            yDeg = ClampAngle(yDeg, yMinLimit, yMaxLimit);
 
        }
        desiredRotation = Quaternion.Euler(yDeg, xDeg, 0);
        currentRotation = transform.rotation;
        rotation = Quaternion.Lerp(currentRotation, desiredRotation, Time.deltaTime * zoomDampening);
        transform.rotation = rotation;
 
 
        if (Input.GetMouseButtonDown (1))
        {
            FirstPosition = Input.mousePosition;
            lastOffset = targetOffset;
        }
 
        if (Input.GetMouseButton (1))
        {
            SecondPosition = Input.mousePosition;
            delta = SecondPosition - FirstPosition;
            targetOffset = lastOffset + transform.right * delta.x*0.3f + transform.up * delta.y*0.3f;
 
        }
 
        ////////Orbit Position
 
        // affect the desired Zoom distance on zooming, does not allow too much or too little zooming
        desiredDistance = Mathf.Clamp(desiredDistance, minDistance, maxDistance);
        
        currentDistance = Mathf.Lerp(currentDistance, desiredDistance, Time.deltaTime * zoomDampening);
        position = target.position - (rotation * Vector3.forward * currentDistance );
        position = position - targetOffset;
        transform.position = position;
 
    }
    private static float ClampAngle(float angle, float min, float max)
    {
        if (angle < -360)
            angle += 360;
        if (angle > 360)
            angle -= 360;
        return Mathf.Clamp(angle, min, max);
    }
    public void RecenterBirdEyeCamera()
    {

        /*Vector3 targetPosition = new Vector3(0, 600, 0);
        float smoothTime = 0.3F;
        Vector3 velocity = Vector3.zero;

    // Smoothly move the camera towards that target position
    transform.position = Vector3.SmoothDamp(transform.position, targetPosition, ref velocity, smoothTime);*/


        /*transform.position = new Vector3(0.0f, 600.0f, 0.0f);
        transform.position = new Vector3(0.0f, 600.0f, 0.0f);*/
        //transform.Translate(new Vector3(0.0f, 600.0f, 0.0f));
        //transform.Rotate(90, 0, 0);


        position = new Vector3(0.0f, 600.0f, 0.0f);
        rotation = Quaternion.Euler(new Vector3(90, 0, 0));

    }
}
 