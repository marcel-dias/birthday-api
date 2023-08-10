from typing import Dict
from diagrams import Cluster, Diagram, Edge, Node
from diagrams.aws.database import Aurora
from diagrams.aws.network import ElbNetworkLoadBalancer, CloudFront
from diagrams.onprem.network import Nginx
from diagrams.programming.framework import Spring
from diagrams.generic.device import Mobile, Tablet

import os
from pprint import pprint

# Graph attr
graph_attr = {
    "style":"rounded",
    # "bgcolor":"transparent",
    "pencolor":"grey",
    "penwidth":"2",
    "fontcolor":"black",
    "fontsize": "12",
    "layout":"dot",
    #  "layout":"fdp",
    "compound":"true",
    "pack": "true",
    "padding": "10",
    "nodesep": "0.65",
    "ranksep": "3",
    "rankdir": "LR",
    # "splines" : "spline",
    "splines": "polyline",
    "direction": "LR",
}

node_attr = {
    "width": "1.5",
    "height": "1.5",
    "imagescale": "true",
    "fixedsize": "true",
    "fontsize": "11",
    "margin": "20",
    "direction": "LR",
}

print("\nGenerating Diagram...")

with Diagram(name="Birthday Message System infrastructure", filename="system", show=False, graph_attr=graph_attr, node_attr=node_attr):
    userMobile = Mobile()
    userTablet = Tablet()

    with Cluster("AWS VPC"):
        cdn = CloudFront("cdn")
        ingressNLB = ElbNetworkLoadBalancer("Ingress NLB")

        with Cluster("AWS EKS") as eks:
            with Cluster("Birthday Message API"):
                birthdayIngress = Nginx("Ingress nginx")
                birthdayPod = Spring("birthday")
                birthdayPod2 = Spring("birthday")

        with Cluster("Persistence Layer"):
            with Cluster("DB Subnet") as subnetDB:
                birthdayDB = Aurora("MysqlDB")
                
    [ userMobile, userTablet ] >> cdn
    cdn >> ingressNLB
    ingressNLB >> birthdayIngress >> [birthdayPod, birthdayPod2] >> birthdayDB
    

    
print("The diagram has been succesfully generated in this path: "+os.getcwd()+"\n")