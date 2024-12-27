import java.sql.Connection;
import java.sql.PreparedStatement;
import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;

public class PutDataToDatabaseUsingJCN_mf_JavaCompute extends MbJavaComputeNode {

    public void evaluate(MbMessageAssembly inAssembly) throws MbException {
        MbOutputTerminal out = getOutputTerminal("out");
        MbOutputTerminal alt = getOutputTerminal("alternate");

        MbMessage inMessage = inAssembly.getMessage();
        MbMessageAssembly outAssembly = null;

        try {
            // Create new message as a copy of the input
            MbMessage outMessage = new MbMessage();
            outAssembly = new MbMessageAssembly(inAssembly, outMessage);

            // ----------------------------------------------------------
            // Add user code below

            // Getting database connection
            Connection conn = getJDBCType4Connection("{JDBCProvidersPolicyProject}:JDBCProvidersPolicy", JDBC_TransactionType.MB_TRANSACTION_AUTO);

            // Correcting the SQL query (fixed typo and removed DOB column)
            String st = "INSERT INTO CUSTOMERS(ID, FIRSTNAME, LASTNAME, AGE, PHONENO, ACCOUNT_NUMBER) VALUES(?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(st);

            // Retrieve XML data
            MbElement root = inMessage.getRootElement().getLastChild();
            MbElement customers = (MbElement)root.getFirstChild();
            MbElement id = (MbElement)customers.getFirstChild();
            MbElement firstname = (MbElement)id.getNextSibling();
            MbElement lastname = (MbElement)firstname.getNextSibling();
            MbElement age = (MbElement)lastname.getNextSibling();
            MbElement phoneno = (MbElement)age.getNextSibling();
            MbElement accountno = (MbElement) phoneno.getNextSibling();

            // Get the actual values (using getValueAsString() instead of toString())
            String id1 = id.getValueAsString();         // Get value of id
            String firstNa = firstname.getValueAsString();  // Get value of firstname
            String last = lastname.getValueAsString();   // Get value of lastname
            String age1 = age.getValueAsString();        // Get value of age
            String phone = phoneno.getValueAsString();    // Get value of phoneno
            String acc = accountno.getValueAsString();  // Get value of accountno

            // Set the values in your PreparedStatement (pstmt)
            pstmt.setString(1, id1);         // id: 1173
            pstmt.setString(2, firstNa);     // firstname: Vishnu
            pstmt.setString(3, last);        // lastname: Ambati
            pstmt.setString(4, age1);        // age: 22
            pstmt.setString(5, phone);       // phoneno: 6546465432
            pstmt.setString(6, acc);         // accountno: 1213124235

            // Execute the update
            pstmt.executeUpdate();

            // ----------------------------------------------------------
        } catch (MbException e) {
            // Re-throw to allow Broker handling of MbException
            throw e;
        } catch (RuntimeException e) {
            // Re-throw to allow Broker handling of RuntimeException
            throw e;
        } catch (Exception e) {
            // Exception handling, re-throw as MbUserException
            throw new MbUserException(this, "evaluate()", "", "", e.toString(), null);
        }

        // The following should only be changed if not propagating message to the 'out' terminal
        out.propagate(outAssembly);
    }

    /**
     * onPreSetupValidation() is called during the construction of the node
     * to allow the node configuration to be validated.  Updating the node
     * configuration or connecting to external resources should be avoided.
     *
     * @throws MbException
     */
    @Override
    public void onPreSetupValidation() throws MbException {
    }

    /**
     * onSetup() is called during the start of the message flow allowing
     * configuration to be read/cached, and endpoints to be registered.
     *
     * Calling getPolicy() within this method to retrieve a policy links this
     * node to the policy. If the policy is subsequently redeployed the message
     * flow will be torn down and reinitialized to it's state prior to the policy
     * redeploy.
     *
     * @throws MbException
     */
    @Override
    public void onSetup() throws MbException {
    }

    /**
     * onStart() is called as the message flow is started. The thread pool for
     * the message flow is running when this method is invoked.
     *
     * @throws MbException
     */
    @Override
    public void onStart() throws MbException {
    }

    /**
     * onStop() is called as the message flow is stopped.
     *
     * The onStop method is called twice as a message flow is stopped. Initially
     * with a 'wait' value of false and subsequently with a 'wait' value of true.
     * Blocking operations should be avoided during the initial call. All thread
     * pools and external connections should be stopped by the completion of the
     * second call.
     *
     * @throws MbException
     */
    @Override
    public void onStop(boolean wait) throws MbException {
    }

    /**
     * onTearDown() is called to allow any cached data to be released and any
     * endpoints to be deregistered.
     *
     * @throws MbException
     */
    @Override
    public void onTearDown() throws MbException {
    }
}
