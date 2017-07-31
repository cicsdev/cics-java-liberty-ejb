# CICS EJB Sample Deployment using z/OS Provisioning Toolkit
The CICS EJB sample can be deployed using the z/OS Provisioning Toolkit.

## Provision
1. Copy the `cics_53_liberty_ejb` directory to z/FS
2. Start a z/OS UNIX shell, using SSH or similar
3. Change directory into the copied directory
4. Build the image: `zospt build -t cics_53_liberty_ejb .`
5. Run the image: `zospt run cics_53_liberty_ejb`
6. (Optionally) Inspect the container: `zospt inspect <containerName>`

## De-provision
1. Stop the image: `zospt stop <containerName>`
2. (Optionally) Remove the image: `zospt rm <containerName>`

## More information on zospt
* [z/OS Provisioning Toolkit - Mainframe DEV](https://developer.ibm.com/mainframe/products/zospt/)
* [z/OS Provisioning Toolkit 1.0 - Knowledge Center](https://www.ibm.com/support/knowledgecenter/en/SSXH44E_1.0.0/zospt/welcome.html)
