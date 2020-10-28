async function postData(url = "", data = {}, token) {
    const response = await fetch(url, {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json",
            ...(token && {'Authorization': token})
        }
    });
    return await response.json();
}

async function getData(url = '',token) {
    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            ...(token && {'Authorization': token})
        }
    });
    return await response.json();
}


export { postData, getData };
