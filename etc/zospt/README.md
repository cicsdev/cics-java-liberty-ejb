# CICS EJB Sample zospt Deployment
The CICS EJB sample can be deployed using `zospt` (z/OS Provisioning Toolkit).

## Deployment
1. Copy the `cics_53_liberty_ejb` directory to z/FS
2. Log on to z/FS (using SSH or similar)
3. Change directory into the copied directory
4. Build the image: `zospt build -t cics_53_liberty_ejb .`
5. Run the image: `zospt run cics_53_liberty_ejb`
6. (Optionally) Inspect the container: `zospt inspect <container>`

## De-provisioning
1. Stop the image: `zospt stop <container>`
2. (Optionally) Remove the image: `zospt rm <container>`

## More information on zospt
* [z/OS Provisioning Toolkit - Mainframe DEV](https://developer.ibm.com/mainframe/products/zospt/)
* [z/OS Provisioning Toolkit 1.0 - Knowledge Center](https://www.ibm.com/support/knowledgecenter/en/SSXH44E_1.0.0/zospt/welcome.html)
